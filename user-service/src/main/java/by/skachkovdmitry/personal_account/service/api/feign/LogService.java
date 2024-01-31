package by.skachkovdmitry.personal_account.service.api.feign;

import by.skachkovdmitry.personal_account.config.FeignClientConfiguration;
import by.skachkovdmitry.personal_account.core.dto.LogInfo;
import feign.RetryableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "audit-service", url = "${feign.clients.audit-service.url}", configuration = FeignClientConfiguration.class)
public interface LogService {

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    void send(LogInfo logInfo) throws RetryableException;
}

