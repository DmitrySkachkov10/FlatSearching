package by.dmitryskachkov.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MainService {
    private final RealtByParser realtByParser;

    private final SaveService saveService;

    public MainService(RealtByParser realtByParser, SaveService saveService) {
        this.realtByParser = realtByParser;
        this.saveService = saveService;
    }

    @Scheduled(fixedDelay = 6000000)
    public void realByParser() {
        saveService.save();
        System.out.println("start realt.by parsing");
        realtByParser.startFlatRentForDayParsing();
        realtByParser.startFlatRentForLongParsing();
        realtByParser.startFlatSalesParsing();
    }
}

