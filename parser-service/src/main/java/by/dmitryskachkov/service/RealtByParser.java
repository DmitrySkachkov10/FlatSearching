package by.dmitryskachkov.service;


import by.dmitryskachkov.repo.entity.FlatEntity;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Service
public class RealtByParser {

    @Value("${urls.realt.basic}")
    private String basicUrl;
    @Value("${urls.realt.rent.for-long}")
    private String rentForLongUrl;

    @Value("${urls.realt.rent.for-day}")
    private String rentForDayUrl;

    @Value("${urls.realt.sales}")
    private String salesUrl;

    private final int PAGE_SIZE = 30;
    private final int PAGE_COUNT_FOR_ONE_THREAD = 75;
    private final BlockingQueue<String> dataQueue = new LinkedBlockingQueue<>();

    public RealtByParser() {
    }

    @Async
    public void startFlatSalesParsing() {
        int flatsCount = getFlatCount(salesUrl);
        int poolSize = flatsCount / (PAGE_SIZE * PAGE_COUNT_FOR_ONE_THREAD) + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < poolSize; i++) {
            int startPage = i * PAGE_COUNT_FOR_ONE_THREAD + 1;
            executorService.execute(() -> {
                parseData(salesUrl, startPage);
            });
        }
    }

    @Async
    public void startFlatRentForLongParsing() {
        int flatsCount = getFlatCount(rentForLongUrl);
        int poolSize = flatsCount / (PAGE_SIZE * PAGE_COUNT_FOR_ONE_THREAD) + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < poolSize; i++) {
            int startPage = i * PAGE_COUNT_FOR_ONE_THREAD + 1;
            executorService.execute(() -> {
                parseData(rentForLongUrl, startPage);
            });
        }
    }

    @Async
    public void startFlatRentForDayParsing() {
        int flatsCount = getFlatCount(rentForDayUrl);
        int poolSize = flatsCount / (PAGE_SIZE * PAGE_COUNT_FOR_ONE_THREAD) + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < poolSize; i++) {
            int startPage = i * PAGE_COUNT_FOR_ONE_THREAD + 1;
            executorService.execute(() -> {
                parseData(rentForDayUrl, startPage);
            });
        }
    }

    private int getFlatCount(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements pElements = document.select("p:contains(объявлений)");

            for (Element pElement : pElements) {

                Element bElement = pElement.selectFirst("b");

                if (bElement != null) {
                    String numberText = bElement.text().trim();
                    int number = Integer.parseInt(numberText);
                    return number;
                }
            }
            return 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseData(String url, int startPage) {
        List<String> urls = getFlatsUrls(url, startPage);
        List<FlatEntity> flats = new ArrayList<>();

        for (String s : urls) {
            flats.add(getFlats(s));
        }
    }

    private List<String> getFlatsUrls(String url, int page) {
        List<String> flatUrls = new ArrayList<>();
        try {
            for (int i = 0; i < PAGE_COUNT_FOR_ONE_THREAD; i++) {
                Document document = Jsoup.connect(url + "?page=" + page + i).get();
                Elements links = document.select("a[href~=rent-flat-for-long/object/\\d+]");

                for (Element link : links) {
                    flatUrls.add(link.attr("href"));
                }
            }
            return flatUrls;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FlatEntity getFlats(String flatUrl) {
        
    }
}

