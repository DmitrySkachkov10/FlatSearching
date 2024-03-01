package by.dmitryskachkov.flatservice.service.api;

import by.dmitryskachkov.flatservice.core.filter.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${feign.clients.user-service.url}")
public interface IUserService {

    @GetMapping("/me")
    User getStatus(@RequestHeader("Authorization") String authorizationHeader);

}
