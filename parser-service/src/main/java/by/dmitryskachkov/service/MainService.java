package by.dmitryskachkov.service;

import by.dmitryskachkov.service.api.IMainService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@EnableScheduling
public class MainService implements IMainService {


    private final RealtByParser realtByParser;

    public MainService(RealtByParser realtByParser) {
        this.realtByParser = realtByParser;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void startRealtByParser() {
     realtByParser.startFlatRentForDayParsing();
     realtByParser.startFlatSalesParsing();
     realtByParser.startFlatRentForLongParsing();
    }


}

