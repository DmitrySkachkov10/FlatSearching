package by.dmitryskachkov.service;

import by.dmitryskachkov.core.RandomUserAgents;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        saveService.save();
        System.out.println("start realt.by parsing");
        realtByParser.startFlatRentForDayParsing();
        realtByParser.startFlatRentForLongParsing();
        realtByParser.startFlatSalesParsing();
    }
}

