package by.skachkovdmitry.mailing_service.service;

import by.skachkovdmitry.mailing_service.repo.api.IMailRepo;
import by.skachkovdmitry.mailing_service.repo.entity.MailVerifyEntity;
import by.skachkovdmitry.mailing_service.service.api.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@EnableScheduling
@Slf4j
public class MailService implements IMailService {

    @Value("${my.verificationPass}")
    private String verificationPass;
    private JavaMailSender javaMailSender;

    private SimpleMailMessage message;

    private final IMailRepo mailRepo;

    public MailService(JavaMailSender javaMailSender, IMailRepo mailRepo) {
        this.javaMailSender = javaMailSender;
        this.mailRepo = mailRepo;
    }

    @Override
    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void send() {

        List<MailVerifyEntity> mailVerifyEntities = mailRepo.findAllBySend(false);

        if (!mailVerifyEntities.isEmpty()){
            mailVerifyEntities.forEach(elem -> {

                message = new SimpleMailMessage();

                message.setTo(elem.getMail());
                message.setSubject("Verification code");
                message.setText(verificationPass + "?" + "code=" + elem.getCode() + "&" + "mail=" + elem.getMail());
                javaMailSender.send(message);

                elem.setSend(true);
                mailRepo.save(elem);
            });
            log.info("Отправка на почту");
        }
    }


    @Override
    public void delete() {

    }
}
