package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.IFlatRepository;
import by.dmitryskachkov.repo.entity.Flat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SaveService {

    private final IFlatRepository flatRepo;
    private final BlockingQueue<Flat> allFlats = new LinkedBlockingQueue<>();

    public SaveService(IFlatRepository flatRepo) {
        this.flatRepo = flatRepo;
    }

    public void putIntoSaveQueue(Flat flatEntity) {
        try {
            allFlats.put(flatEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Runnable save() {
        try {
            while (true) {
                Flat flat = allFlats.poll(90, TimeUnit.SECONDS);
                if (flat == null) {
                    log.info("Exiting...");
                    break;
                }

                Flat existingFlat = flatRepo.findByOriginalUrl(flat.getOriginalUrl());
                if (existingFlat != null && Objects.equals(existingFlat.getPrice(), flat.getPrice())) {
                  //todo same
                    continue;
                }

                if (existingFlat != null) {
                    existingFlat.setPrice(flat.getPrice());
                    existingFlat.setUpdateDate(LocalDateTime.now());
                    save(existingFlat);
                    //todo update
                } else {
                    save(flat);
                    //todo save
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting for flats in queue.", e);
        }
        return null;
    }


    @Transactional
    private void save(Flat flat) {
        try {
            flatRepo.save(flat);
        } catch (Throwable e) {
            log.error("Exception: " + e);
        }
    }



}
