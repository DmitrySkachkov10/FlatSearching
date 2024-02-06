package by.dmitryskachkov.service;


import by.dmitryskachkov.core.RandomUserAgents;
import by.dmitryskachkov.core.enums.OfferType;
import by.dmitryskachkov.core.util.NumberUtils;
import by.dmitryskachkov.repo.api.IFlatRepo;
import by.dmitryskachkov.repo.entity.FlatEntity;
import by.dmitryskachkov.repo.entity.Photos;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;


@Service
public class RealtByParser {

    @Value("${app.urls.realt.basic}")
    private String basicUrl;
    @Value("${app.urls.realt.rent.for-long}")
    private String rentForLongUrl;

    @Value("${app.urls.realt.rent.for-day}")
    private String rentForDayUrl;

    @Value("${app.urls.realt.sales}")
    private String salesUrl;

    private final int PAGE_SIZE = 30;

    private final int PAGE_COUNT_FOR_ONE_THREAD = 75;
    private final BlockingQueue<FlatEntity> dataQueue = new LinkedBlockingQueue<>();

    private final IFlatRepo flatRepo;

    public RealtByParser(IFlatRepo flatRepo) {
        this.flatRepo = flatRepo;
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
        startFlatParsing(rentForDayUrl, OfferType.RENT);
    }


    @Async
    public void saveData() {
        System.out.println("Start");
        while (true) {
            try {
                FlatEntity data = dataQueue.take();
                flatRepo.save(data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void startFlatParsing(String url, OfferType offerType) {
        int flatsCount = getFlatCount(url);
        int poolSize = flatsCount / (PAGE_SIZE * PAGE_COUNT_FOR_ONE_THREAD) + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        for (int i = 0; i < poolSize; i++) {
            int startPage = i * PAGE_COUNT_FOR_ONE_THREAD + 1;
            executorService.execute(() -> {
                List<FlatEntity> flatEntities = parseData(url, startPage);
                flatEntities.forEach(f -> f.setOfferType(offerType));
                dataQueue.addAll(flatEntities);
            });
        }
        executorService.shutdown();
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
            System.out.println("Error в getFlatCount");
        }
        return 0;
    }

    private List<FlatEntity> parseData(String url, int startPage) {
        List<String> urls = getFlatsUrls(url, startPage);
        List<FlatEntity> flats = new ArrayList<>();
        int i = 0;
        for (String s : urls) {
            if (i % 5 == 0) {
                try {
                    Thread.sleep(30000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            flats.add(getFlats(s));
            i++;
        }
        return flats;
    }

    private List<String> getFlatsUrls(String url, int page) {
        List<String> flatUrls = new ArrayList<>();
        try {
            for (int i = 0; i < PAGE_COUNT_FOR_ONE_THREAD; i++) {
                if (i % 5 == 0) {
                    TimeUnit.SECONDS.sleep(30);
                }

                System.out.println(url + "?page=" + page + i);
                Document document = Jsoup.connect(url + "?page=" + page + i).userAgent(RandomUserAgents.getRandomUserAgent()).get();
                Elements links = document.select("a[href~=rent-flat-for-long/object/\\d+]");

                for (Element link : links) {
                    flatUrls.add(link.attr("href"));
                }
            }
            return flatUrls;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error в getFlatsUrls");
        }
        return flatUrls;
    }

    private FlatEntity getFlats(String flatUrl) {
        FlatEntity flatEntity = new FlatEntity();
        try {
            String originalUrl = basicUrl + flatUrl;
            Document document = Jsoup.connect(originalUrl).userAgent(RandomUserAgents.getRandomUserAgent()).get();
            flatEntity.setUuid(UUID.randomUUID());
            flatEntity.setCreateDate(LocalDateTime.now());
            flatEntity.setUpdateDate(LocalDateTime.now());
            flatEntity.setOriginalUrl(originalUrl);
            flatEntity.setPhotos(getPhotoUrls(document, flatEntity));
            setUpData(flatEntity, document);
            return flatEntity;
        } catch (IOException e) {
            System.out.println("Error в getFlats");
        }
        return flatEntity;
    }


    private void setUpData(FlatEntity flat, Document document) {
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
    }

    private List<Photos> getPhotoUrls(Document document, FlatEntity flatEntity) {
        Elements imgElements = document.select("img");
        List<Photos> imageUrls = new ArrayList<>();
        for (Element imgElement : imgElements) {
            String src = imgElement.attr("src");
            if (!src.isEmpty()) {
                imageUrls.add(new Photos(src, flatEntity));
            }
        }
        return imageUrls;
    }


}

