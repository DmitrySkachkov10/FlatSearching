package by.dmitryskachkov.flatservice.service.api;

import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mail-service", url = "${feign.clients.mail-service.url}")
public interface IMailService {

    @PostMapping("/send")
    void send(PageOfFlat data, String mail);
}

