package by.dmitryskachkov.service;

import by.dmitryskachkov.repo.api.IFlatRepository;
import by.dmitryskachkov.repo.entity.Flat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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

    public void save() {
        try {
            while (true) {
                Flat flat = allFlats.poll(10, TimeUnit.SECONDS);
                if (flat != null) {
                    Flat existingFlat = flatRepo.findByOriginalUrl(flat.getOriginalUrl());
                    if (existingFlat != null) {
                        if (!Objects.equals(existingFlat.getPrice(), flat.getPrice())) {
                            existingFlat.setPrice(flat.getPrice());
                            existingFlat.setUpdateDate(LocalDateTime.now());
                            save(existingFlat);
                            System.out.println("Update existing flat");
                        } else {
                            System.out.println("Flat with same original_url and same price already exists. Skipping...");
                        }
                    } else {
                        // Объекта с таким original_url нет, сохраняем новый
                        save(flat);
                        System.out.println("Save new flat");
                    }
                } else {
                    System.out.println("No flats found in queue. Exiting...");
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for flats in queue.");
        }
    }

    @Transactional
    private void save(Flat flat) {
        try {
            flatRepo.save(flat);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
    }



}
