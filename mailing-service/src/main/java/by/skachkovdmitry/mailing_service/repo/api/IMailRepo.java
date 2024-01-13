package by.skachkovdmitry.mailing_service.repo.api;

import by.skachkovdmitry.mailing_service.repo.entity.MailVerifyEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface IMailRepo extends ListCrudRepository<MailVerifyEntity, UUID> {

    List<MailVerifyEntity> findAllBySend(Boolean send);

    boolean existsByMailAndCode(String mail, String code);

}
