package by.dmitryskachkov.service;


import by.dmitryskachkov.core.RandomUserAgents;
import by.dmitryskachkov.core.enums.OfferType;
import by.dmitryskachkov.core.util.NumberUtils;
import by.dmitryskachkov.core.util.FindData;
import by.dmitryskachkov.repo.entity.FlatEntity;
import by.dmitryskachkov.repo.entity.Photos;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;


@Service
@EnableAsync
public class RealtByParser {

    @Value("${app.urls.realt.basic}")
    private String basicUrl;
    @Value("${app.urls.realt.rent.for-long}")
    private String rentForLongUrl;

    @Value("${app.urls.realt.rent.for-day}")
    private String rentForDayUrl;

    @Value("${app.urls.realt.sales}")
    private String salesUrl;
    private final BlockingQueue<String> rentLinks = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> saleLinks = new LinkedBlockingQueue<>();

    private final SaveService saveService;

    public RealtByParser(SaveService saveService) {
        this.saveService = saveService;
    }

    @Async
    public void startFlatSalesParsing() {
        System.out.println("1111111111111111111111111");
        startFlatParsing(salesUrl, OfferType.SALE);
    }

    @Async
    public void startFlatRentForLongParsing() {
        System.out.println("2222222222222222222222222");
        startFlatParsing(rentForLongUrl, OfferType.RENT);
    }

    @Async
    public void startFlatRentForDayParsing() {
        System.out.println("55555555555555555555555555");
        startFlatParsing(rentForDayUrl, OfferType.RENT_FOR_DAY);
    }

    private void startFlatParsing(String url, OfferType offerType) {

        int flatsCount = getFlatCount(url);
        int poolSize = flatsCount / 100 + 1;

        System.out.println("poolsize = " + poolSize);
        ExecutorService findUrlsSerive = Executors.newFixedThreadPool(poolSize);
        ExecutorService parseDataService = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < poolSize; i++) {
            int startPage = i * 100 + 1;
            System.out.println("startPage = " + startPage);

            findUrlsSerive.execute(() ->
                    findFlatsUrls(new FindData(url, startPage, offerType)));
            parseDataService.execute(() ->
                    parseFlats(offerType));
        }

        parseDataService.shutdown();
        findUrlsSerive.shutdown();
    }

    private void findFlatsUrls(FindData findData) {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(3000);
                System.out.println(findData.getUrl() + "?page=" + findData.getStartPage() + i);
                Document document = Jsoup.connect(findData.getUrl() + "?page=" + findData.getStartPage() + i).userAgent(RandomUserAgents.getRandomUserAgent()).get();
                if (!document.data().isEmpty()) {
                    String urlPart = findData.getOfferType().getParameter();
                    Elements links = document.select("a[href~=" + urlPart + "\\d+]");
                    for (Element link : links) {
                        putIntoQueue(link.attr("href"), findData.getOfferType());
                    }
                } else {
                    return;
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error в getFlatsUrls");
        }
    }

    private void putIntoQueue(String url, OfferType offerType) throws InterruptedException {
        if (offerType.equals(OfferType.RENT) || offerType.equals(OfferType.RENT_FOR_DAY)) {
            rentLinks.put(url);
        } else {
            saleLinks.put(url);
        }
        System.out.println("FIRSTQUEUE PUTED - " + url);
    }

    private void parseFlats(OfferType offerType) {
        while (true) {
            try {
                if (offerType.equals(OfferType.RENT)) {
                    String url = rentLinks.poll(120, TimeUnit.SECONDS);
                    if (url.isEmpty()) {
                        break;
                    }
                    setUpData(url, OfferType.RENT);
                } else {
                    String url = rentLinks.poll(120, TimeUnit.SECONDS);
                    if (url.isEmpty()) {
                        break;
                    }
                    setUpData(url, OfferType.SALE);
                }
            } catch (InterruptedException e) {
                System.out.println("Ошибка #4341");
            }
        }
    }

    private void setUpData(String flatUrl, OfferType offerType) {

        try {
            FlatEntity flat = new FlatEntity();
            String originalUrl = basicUrl + flatUrl;
            flat.setUuid(UUID.randomUUID());
            flat.setCreateDate(LocalDateTime.now());
            flat.setUpdateDate(LocalDateTime.now());
            flat.setOriginalUrl(originalUrl);
            flat.setOfferType(offerType);

            Document document = Jsoup.connect(originalUrl).userAgent(RandomUserAgents.getRandomUserAgent()).get();
            Elements liElements = document.select("li.relative");
            for (Element liElement : liElements) {
                Element spanElement = liElement.selectFirst("span");
                Element pElement = liElement.selectFirst("p");

                if (spanElement != null && pElement != null) {
                    String spanText = spanElement.text().trim();
                    String pText = pElement.text().trim();

                    switch (spanText) {
                        case "Количество комнат":
                            flat.setBedrooms((int) NumberUtils.extractNumberFromString(pText));
                            break;
                        case "Этажность":
                            flat.setFloor((int) NumberUtils.extractNumberFromString(pText));
                            break;
                        case "Этаж / этажность":
                            flat.setFloor((int) NumberUtils.extractNumberFromString(pText.split("/")[0].trim()));
                            break;
                        case "Площадь общая":
                            flat.setArea((float) NumberUtils.extractNumberFromString(pText));
                            break;
                        default:
                            break;
                    }
                }
            }

            Elements elements = document.select("h2");
            if (elements.isEmpty()) {
                flat.setPrice("Договорная");
            } else {
                flat.setPrice(elements.get(0).text());
            }

            Elements imgElements = document.select("img");
            Set<Photos> photos = new HashSet<>();
            for (Element imgElement : imgElements) {
                String src = imgElement.attr("src");
                if (!src.isEmpty()) {
                    photos.add(new Photos(src, flat));
                }
            }

            flat.setPhotos(photos);

            System.out.println("PARSING FROM QUEUE - " + flat.toString());
            saveService.putIntoSaveQueue(flat);
        } catch (IOException e) {

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
        } catch (IOException e) {
            System.out.println("Error в getFlatCount");
        }
        return 0;
    }
}

