package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.*;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface IUserService {
    UserEntity getUserByMail(String mail);

    UserEntity getUserByUuid(UUID uuid);

    PageOfUser getUsers(Pageable pageable);

    void save(UserEntity user);

    UserEntity logIn(UserLogin userLogin);

    void update(UserEntity userEntity);

    void updateUserStatus(String mail, Status status);
}
