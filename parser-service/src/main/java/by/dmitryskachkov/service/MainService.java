package by.dmitryskachkov.service;


import by.dmitryskachkov.repo.api.IPhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@EnableScheduling
@Slf4j
public class MainService {
    private final RealByParser realByParser;

    private final IPhotoRepository photoRepo;
    private final SaveService saveService;

    public MainService(RealByParser realByParser, IPhotoRepository photoRepo, SaveService saveService) {
        this.realByParser = realByParser;
        this.photoRepo = photoRepo;
        this.saveService = saveService;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void realByParser() throws InterruptedException {
        System.out.println("start realt.by parsing at: " + LocalDateTime.now());
        log.info("start realt.by parsing at: " + LocalDateTime.now());
        realByParser.startFlatRentForDayParsing();
        realByParser.startFlatRentForLongParsing();
        realByParser.startFlatSalesParsing();
        Thread thread = new Thread(saveService::save);
        thread.start();
        thread.join();
        new Thread(photoRepo::removeDuplicatePhotos).start();
        System.out.println("end of parsing at: " + LocalDateTime.now());
        log.info("end of parsing at: " + LocalDateTime.now());
    }
}

