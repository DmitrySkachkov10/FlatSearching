package by.skachkovdmitry.personal_account.service.api.feign;

import by.skachkovdmitry.personal_account.core.dto.verification.MailVerifyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "mailing-service", url = "${feign.clients.mailing-service.url}")
public interface ISendService {

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    ResponseEntity<String> send(MailVerifyDTO mailVerifyDTO);
}
