package by.skachkovdmitry.mailing_service.controller;

import by.skachkovdmitry.mailing_service.service.api.IMailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mail")
public class SendController {

    private final IMailService mailService;

    public SendController(IMailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public void send(@RequestBody String data,
                     @RequestBody String mail) {
        mailService.send(data, mail);
    }
}
