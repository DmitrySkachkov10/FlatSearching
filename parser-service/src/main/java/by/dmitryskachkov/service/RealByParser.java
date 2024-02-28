package by.dmitryskachkov.service;

import by.dmitryskachkov.core.RandomUserAgents;
import by.dmitryskachkov.core.enums.OfferType;
import by.dmitryskachkov.core.util.FindData;
import by.dmitryskachkov.core.util.NumberUtils;
import by.dmitryskachkov.entity.SystemError;
import by.dmitryskachkov.repo.entity.Flat;
import by.dmitryskachkov.repo.entity.Photo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@EnableAsync
@Slf4j
public class RealByParser {
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

    public RealByParser(SaveService saveService) {
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

        try {
            findUrlsService.shutdown();
            parseDataService.shutdown();
            findUrlsService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            parseDataService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("All threads have finished their work at: " + LocalDateTime.now());
    }

    private void findFlatsUrls(FindData findData) {

        for (int i = 0; i < 1; i++) {
            try {
                int pageNumber = findData.getStartPage() + i;
                if (findData.getPageCount() < pageNumber) {
                    break;
                }
                sleepIteration(i, 10);
                String findingUrl = findData.getUrl() + "?page=" + pageNumber;
                Document document = getDocument(findingUrl);
                Elements links = document.select("a[href~=" + findData.getOfferType().getParameter() + "\\d+] ");

                for (Element link : links) {
                    String findUrl = (link.attr("href")).replace(basicUrl, "");
                    putIntoQueue(findUrl, findData.getOfferType());
                }

            } catch (InterruptedException | IOException e) {
                log.error("Ошибка в получении url из ресурса realt.by" + e);
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
                    break;
                }
                Flat flat;
                if ((flat = setUpData(url, offerType)) == null) {
                    continue;
                }
                saveService.putIntoSaveQueue(flat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("stop parse flats at: " + LocalDateTime.now());
    }

    private Flat setUpData(String flatUrl, OfferType offerType) {
        Flat flat = new Flat();
        try {
            Thread.sleep(50);
            flat.setUuid(UUID.randomUUID());
            flat.setCreateDate(LocalDateTime.now());
            flat.setUpdateDate(LocalDateTime.now());
            flat.setOriginalUrl(flatUrl);
            flat.setOfferType(offerType);

            Document document = getDocument(flatUrl);

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
                            flat.setArea(NumberUtils.extractNumberFromString(pText).floatValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            Elements elements = document.select("h2");
            if (elements.isEmpty()) {
                flat.setPrice(-1);
            } else {
                flat.setPrice(Integer.parseInt(elements.get(0).text().replaceAll("[^0-9]", "")));
            }
            flat.setPhotos(getPhotos(document, flat));
            return flat;
        } catch (InterruptedException | IOException e) {
            log.error("An error occurred:" + e);
        }
        return null;
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
            log.error("An error occurred:" + e);
        }
        return 0;
    }

    private void sleepIteration(int yourIteration, int iteration) throws InterruptedException {
        if (yourIteration > 0 && (yourIteration % iteration == 0)) {
            Thread.sleep(60000);
        }
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).userAgent(RandomUserAgents.getRandomUserAgent()).get();
    }

    /**
     * @param document find photos on this document
     * @param flat     -> to set photos for this flat
     * @return Set<Photos> for this flat
     */
    private Set<Photo> getPhotos(Document document, Flat flat) {
        Elements imgElements = document.select("img");
        Set<Photo> photos = new HashSet<>();
        for (Element imgElement : imgElements) {
            String src = imgElement.attr("src");
            if (!src.isEmpty() && !src.contains("thumb/c/160x160") && src.contains(".jpg")) {
                photos.add(new Photo(UUID.randomUUID(), src, flat));
            }
        }
        return photos;
    }
}
