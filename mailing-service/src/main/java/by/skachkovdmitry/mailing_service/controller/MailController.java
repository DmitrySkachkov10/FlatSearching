//package by.skachkovdmitry.mailing_service.controller;
//
//import by.skachkovdmitry.mailing_service.service.api.IMailService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/mail")
//public class MailController {
//    private final IMailService mailService;
//
//    public MailController(IMailService mailService) {
//        this.mailService = mailService;
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<String> send(@RequestBody String mail, String code) {
//
//        try {
//            mailService.create(mail, code);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok("Message was sent");
//    }
//}
