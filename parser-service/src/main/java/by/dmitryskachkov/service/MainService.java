package by.dmitryskachkov.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MainService {
    private final RealtByParser realtByParser;

    public MainService(RealtByParser realtByParser) {
        this.realtByParser = realtByParser;
    }

    @Scheduled(fixedDelay = 6000000)
    public void test() {
        System.out.println("Start2");
        realtByParser.startFlatRentForDayParsing();
        realtByParser.startFlatRentForLongParsing();
        realtByParser.startFlatSalesParsing();
        realtByParser.saveData();
    }
}

