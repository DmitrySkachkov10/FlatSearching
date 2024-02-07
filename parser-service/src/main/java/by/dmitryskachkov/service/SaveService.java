package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.IFlatRepo;
import by.dmitryskachkov.repo.entity.FlatEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

@Service
public class SaveService {

    private final IFlatRepo flatRepo;
    private final BlockingQueue<FlatEntity> allFlats = new LinkedBlockingQueue<>();

    public SaveService(IFlatRepo flatRepo) {
        this.flatRepo = flatRepo;
    }

    public void putIntoSaveQueue(FlatEntity flatEntity) {
        try {
            allFlats.put(flatEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Transactional
    public void save() {
        ExecutorService saveService = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 15; i++) {
            saveService.execute(() -> {
                while (true) {
                    System.out.println("SAVE TO DB");
                    try {
                        FlatEntity flatEntity = allFlats.poll(30, TimeUnit.SECONDS);
                        if (flatEntity == null) {
                            break;
                        }
                        flatRepo.save(flatEntity);
                    } catch (InterruptedException e) {
                    }
                }
                System.err.println("END FULL ");
            });
        }
        saveService.shutdown();
    }
}
