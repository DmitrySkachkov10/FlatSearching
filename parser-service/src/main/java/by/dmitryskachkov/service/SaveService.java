package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.IFlatRepo;
import by.dmitryskachkov.repo.entity.FlatEntity;
import by.dmitryskachkov.service.api.ISaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

@Service
@Slf4j
public class SaveService implements ISaveService {

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
    @Override
    public void save() {
        while (true) {
            try {
                FlatEntity flatEntity = allFlats.poll(10, TimeUnit.SECONDS);
                if (flatEntity == null) {
                    System.err.println("end of saving thread");
                    break;
                }
                FlatEntity existingEntity = flatRepo.findById(flatEntity.getOriginalUrl()).orElse(null);

                if (existingEntity == null) {
                    flatRepo.save(flatEntity);
                    log.info("save");
                    continue;
                }

                if (!existingEntity.getPrice().equals(flatEntity.getPrice())) {
                    flatRepo.save(flatEntity);
                    log.info("update");
                }

            } catch (Throwable e) {
                System.err.println("2" + e.getMessage());
            }
        }
    }
}

