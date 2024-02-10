package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.repo.entity.MailVerifyEntity;

public interface  IMailService {
    boolean exists(MailVerifyEntity mailVerifyEntity);
    void create(String mail);
}
