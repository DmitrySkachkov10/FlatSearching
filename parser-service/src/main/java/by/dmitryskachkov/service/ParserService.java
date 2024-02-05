package by.dmitryskachkov.service;

import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Service
@EnableScheduling
public class ParserService {

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
            System.out.println( endTime - startTime);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        try {
//            // Замените url на ваш URL
//
//
//
//            Document document = Jsoup.connect(url).get();
//
//            // Используйте CSS-селектор для поиска <p> с текстом "объявлений"
//            Elements pElements = document.select("p:contains(объявлений)");
//
//            // Перебирайте найденные <p>
//            for (Element pElement : pElements) {
//                // Ищем тег <b> внутри каждого <p>
//                Element bElement = pElement.selectFirst("b");
//
//                if (bElement != null) {
//                    // Если <b> найден, извлекаем текст и завершаем парсинг
//                    String numberText = bElement.text().trim();
//                    int number = Integer.parseInt(numberText);
//                    System.out.println("Found number: " + number);
//
//                    long endTime = System.currentTimeMillis();
//
//                    // Вычисляем разницу времени
//                    System.out.println( endTime - startTime);
//                    return; // Прекращаем парсинг
//                }
//            }
//
//            // Если не найдено, вы можете обработать этот случай
//            System.out.println("Не найдено соответствующих данных");
//
//
//            long endTime = System.currentTimeMillis();
//
//            // Вычисляем разницу времени
//            long elapsedTime = endTime - startTime;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}

