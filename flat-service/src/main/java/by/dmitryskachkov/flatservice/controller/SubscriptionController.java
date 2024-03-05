package by.dmitryskachkov.flatservice.controller;

import by.dmitryskachkov.flatservice.core.SubscriptionMapper;
import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfSubscription;
import by.dmitryskachkov.flatservice.core.dto.SubscriptionDTO;
import by.dmitryskachkov.flatservice.service.api.ISubscriptionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {
    private final ISubscriptionService subscriptionService;

    public SubscriptionController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping()
    public ResponseEntity<String> addSubscription(FlatFilter flatFilter) {
        SubscriptionDTO subscriptionDTO = SubscriptionMapper.INSTANCE.mapToSubscriptionDTO(flatFilter);
        subscriptionService.addSubscription(subscriptionDTO);
        return new ResponseEntity<>("Подписка добавлена", HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<String> deleteSubscription(@PathVariable UUID uuid, @PathVariable("dt_update") long dtUpdate) {
        subscriptionService.deleteSubscription(uuid, dtUpdate);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageOfSubscription> getSubscriptions(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "30") int size){
        return new ResponseEntity<>(subscriptionService.getSubscriptions(PageRequest.of(page-1, size)),
                HttpStatus.OK);

    }


    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<String> updateSubscription(@PathVariable UUID uuid,
                                                     @PathVariable("dt_update") long dtUpdate,
                                                     @RequestBody SubscriptionDTO subscriptionDTO) {
        subscriptionService.updateSubscription(subscriptionDTO, uuid, dtUpdate);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}


