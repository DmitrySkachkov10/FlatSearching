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

    private int PAGE_SIZE = 30;

    private int POOL_SIZE = 10;

    private final BlockingQueue<String> rentLinks = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> saleLinks = new LinkedBlockingQueue<>();

    private final SaveService saveService;

    public RealtByParser(SaveService saveService) {
        this.saveService = saveService;
    }

    @Async
    public void startFlatSalesParsing() {
        startFlatParsing(salesUrl, OfferType.SALE);
    }

    @Async
    public void startFlatRentForLongParsing() {
        startFlatParsing(rentForLongUrl, OfferType.RENT);
    }

    @Async
    public void startFlatRentForDayParsing() {
        startFlatParsing(rentForDayUrl, OfferType.RENT_FOR_DAY);
    }

    private void startFlatParsing(String url, OfferType offerType) {

        int flatsCount = getFlatCount(url);
        int allPageCount = flatsCount / PAGE_SIZE + 1;
        int pageCountForThread = flatsCount / (PAGE_SIZE * POOL_SIZE) + 1;

        ExecutorService findUrlsService = Executors.newFixedThreadPool(POOL_SIZE);
        ExecutorService parseDataService = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < POOL_SIZE; i++) {
            int startPage = i * pageCountForThread + 1;
            findUrlsService.execute(() ->
                    findFlatsUrls(new FindData(url, startPage, offerType, allPageCount, pageCountForThread)));
            parseDataService.execute(() ->
                    parseFlats(offerType));
        }

        parseDataService.shutdown();
        findUrlsService.shutdown();
    }

    private void findFlatsUrls(FindData findData) {

        for (int i = 0; i < findData.getPageCountForThread(); i++) {
            try {

                int pageNumber = findData.getStartPage() + i;
                if (findData.getPageCount() < pageNumber) {
                    break;
                }

                //todo  вынести в отдельный метод
                if (i > 0 && (i % 10 == 0)) {
                    System.out.println("Sleeping at iteration " + i);
                    Thread.sleep(60000);
                }

                String findingUrl = findData.getUrl() + "?page=" + pageNumber;
                Document document = Jsoup.connect(findingUrl)
                        .userAgent(RandomUserAgents.getRandomUserAgent())
                        .get();

                //todo вынсти в метод для поиска ссылок
                Elements links = document.select("a[href~=" + ".*" + findData.getOfferType().getParameter() + "\\d+]");
                for (Element link : links) {
                    String findUrl = (link.attr("href")).replace(basicUrl, "");
                    putIntoQueue(findUrl, findData.getOfferType());
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error в getFlatsUrls");
            }
        }
    }

    private void putIntoQueue(String url, OfferType offerType) throws InterruptedException {
        if (offerType.equals(OfferType.RENT) || offerType.equals(OfferType.RENT_FOR_DAY)) {
            rentLinks.put(basicUrl + url);
        } else {
            saleLinks.put(basicUrl + url);
        }
    }

    private void parseFlats(OfferType offerType) {
        while (true) {
            try {
                String url;
                if (offerType.equals(OfferType.RENT) || offerType.equals(OfferType.RENT_FOR_DAY)) {
                    url = rentLinks.poll(30, TimeUnit.SECONDS);
                } else {
                    url = saleLinks.poll(30, TimeUnit.SECONDS);
                }
                if (url == null || url.isEmpty()) {
                    System.err.println("ERROR IN parseFlats");
                    break;
                }
                setUpData(url, offerType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println("END SETUPS");
    }

    private void setUpData(String flatUrl, OfferType offerType) {
        FlatEntity flat = new FlatEntity();
        try {
            Thread.sleep(50);
            flat.setUuid(UUID.randomUUID());
            flat.setCreateDate(LocalDateTime.now());
            flat.setUpdateDate(LocalDateTime.now());
            flat.setOriginalUrl(flatUrl);
            flat.setOfferType(offerType);

            //todo в отделный метод что возвращает Document по URL или в отдельный класс вообще
            Document document = Jsoup.connect(flatUrl).userAgent(RandomUserAgents.getRandomUserAgent()).get();
            Elements liElements = document.select("li.relative");
            for (Element liElement : liElements) {
                Element spanElement = liElement.selectFirst("span");
                Element pElement = liElement.selectFirst("p");

                if (spanElement != null && pElement != null) {
                    String spanText = spanElement.text().trim();
                    String pText = pElement.text().trim();

                    switch (spanText) {
                        case "Количество комнат":
                            flat.setBedrooms(NumberUtils.extractNumberFromString(pText).intValue());
                            break;
                        case "Этажность":
                            flat.setFloor(NumberUtils.extractNumberFromString(pText).intValue());
                            break;
                        case "Этаж / этажность":
                            flat.setFloor(NumberUtils.extractNumberFromString(pText.split("/")[0].trim()).intValue());
                            break;
                        case "Площадь общая":
                            System.out.println(pText.toUpperCase());
                            flat.setArea(NumberUtils.extractNumberFromString(pText).floatValue());
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


            //todo отдельный метод для фоток
            Elements imgElements = document.select("img");
            Set<Photos> photos = new HashSet<>();
            for (Element imgElement : imgElements) {
                String src = imgElement.attr("src");
                if (!src.isEmpty() && !src.contains("thumb/c/160x160")) {
                    photos.add(new Photos(UUID.randomUUID(), src, flat));
                }
            }

            flat.setPhotos(photos);
            System.out.println("Putted into 2 QUEUE");
            saveService.putIntoSaveQueue(flat);
        } catch (IOException | InterruptedException error) {
            System.err.println("error -> " + flatUrl);
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
            e.printStackTrace();
        }
        return 0;
    }
}

