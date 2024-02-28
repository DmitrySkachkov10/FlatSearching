package by.dmitryskachkov.flatservice.controller;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flats")
public class FlatController {

    private final IFlatService flatService;

    public FlatController(IFlatService flatService) {
        this.flatService = flatService;
    }

    @GetMapping
    public ResponseEntity<PageOfFlat> getFlats(FlatFilter flatFilter) {
        return new ResponseEntity<>(flatService.getPageOfFlat(flatFilter), HttpStatus.OK);
    }

}
