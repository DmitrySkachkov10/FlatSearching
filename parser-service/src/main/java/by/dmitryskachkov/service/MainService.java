package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.PhotoRepo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MainService {
    private final RealtByParser realtByParser;

    private final PhotoRepo photoRepo;
    private final SaveService saveService;

    public MainService(RealtByParser realtByParser, PhotoRepo photoRepo, SaveService saveService) {
        this.realtByParser = realtByParser;
        this.photoRepo = photoRepo;
        this.saveService = saveService;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void realByParser() {
        System.out.println("start realt.by parsing");

        saveService.save();
        realtByParser.startFlatRentForDayParsing();
        realtByParser.startFlatRentForLongParsing();
        realtByParser.startFlatSalesParsing();
        photoRepo.removeDuplicatePhotos();
        System.out.println("end");
    }
}

