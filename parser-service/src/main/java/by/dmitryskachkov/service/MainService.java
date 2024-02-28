package by.dmitryskachkov.service;


import by.dmitryskachkov.repo.api.IPhotoRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
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
        System.out.println("start realt.by parsing");
        realByParser.startFlatRentForDayParsing();
        realByParser.startFlatRentForLongParsing();
        realByParser.startFlatSalesParsing();
        new Thread(saveService::save).start();
        Thread.sleep(30000);
        new Thread(photoRepo::removeDuplicatePhotos).start();
        System.out.println("end");
    }
}

