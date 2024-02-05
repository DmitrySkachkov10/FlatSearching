package by.dmitryskachkov.service;


import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service

public class ParserService {

    private final BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>();
    private final Lock databaseLock = new ReentrantLock();

    public void parseData(String url, int poolSize) {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            int startPage = i;
            executorService.execute(() -> parse(url, startPage * 50 + 1));
        }

    }


    private void parse(String urlPart, int page) {
        try {
            for (int i = 0; i < 51; i++) {
                Document document = Jsoup.connect(url + "?page=" + page + i).get();
                //todo парсер квартиры
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private final String url = "https://realt.by/belarus/rent/flat-for-long/?page=1";

    @Scheduled(fixedDelay = 60000)
    public void goAndPrint() {
        System.out.println("START!");
        try {
            long startTime = System.currentTimeMillis();
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href~=rent-flat-for-long/object/\\d+]");

            for (Element link : links) {
                String href = link.attr("href");
                System.out.println("Found link: " + href);
            }

            long endTime = System.currentTimeMillis();

            // Вычисляем разницу времени
            System.out.println(endTime - startTime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

