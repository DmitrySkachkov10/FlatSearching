package by.dmitryskachkov.service;

import by.dmitryskachkov.service.api.IMainService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@EnableScheduling
public class MainService implements IMainService {

    @Value("${urls.rent.for-long}")
    private String rentForLongUrl;

    @Value("${urls.rent.for-day}")
    private String rentForDayUrl;

    @Value("${urls.sales}")
    private String salesUrl;


    private final ParserService parserService;

    public MainService(ParserService parserService) {
        this.parserService = parserService;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void rentForLong() {
        parserService.parseData(rentForLongUrl, adCount(rentForLongUrl) / 1500 + 1);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void rent() {
        parserService.parseData(rentForDayUrl, adCount(rentForDayUrl) / 1500 + 1);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void byFlat() {
        parserService.parseData(salesUrl, adCount(salesUrl) / 1500 + 1);
    }


    private int adCount(String url) {
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
}

