package by.skachkovdmitry.personal_account.service;

import by.skachkovdmitry.personal_account.repo.api.IMailRepo;
import by.skachkovdmitry.personal_account.repo.entity.MailVerifyEntity;
import by.skachkovdmitry.personal_account.service.api.IMailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MailService implements IMailService {

    private final IMailRepo mailRepo;

    public MailService(IMailRepo mailRepo) {
        this.mailRepo = mailRepo;
    }

    @Override
    public boolean exists(MailVerifyEntity mailVerifyEntity) {
        return mailRepo.existsByMailAndCode(mailVerifyEntity.getMail(), mailVerifyEntity.getCode());
    }

    @Override
    @Transactional
    public void create(String mail) {
        MailVerifyEntity mailVerifyEntity = new MailVerifyEntity();
        mailVerifyEntity.setMail(mail);
        mailVerifyEntity.setUuid(UUID.randomUUID());
        mailVerifyEntity.setCode(generateCode());
        mailVerifyEntity.setSend(false);
        mailRepo.save(mailVerifyEntity);
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

}
