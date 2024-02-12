package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.PhotoRepo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
public class MainService {
    private final RealtByParser realtByParser;

    private final SaveService saveService;

    public MainService(RealtByParser realtByParser, SaveService saveService) {
        this.realtByParser = realtByParser;
        this.saveService = saveService;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void realByParser() {
        System.out.println("start realt.by parsing");
        saveService.save();
        realtByParser.startFlatRentForDayParsing();
        realtByParser.startFlatRentForLongParsing();
        realtByParser.startFlatSalesParsing();

        System.out.println("end");
    }
}

