package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.*;
import by.skachkovdmitry.personal_account.core.status.Status;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IUserService {
    User getUserByMail(String mail);

    PageOfUser getUsers(Pageable pageable);

    void save(UserRegistration userRegistration);

    void save(UserCreate userCreate);

    boolean exists(UserLogin userLogin);

    void updateUserStatus(String mail, Status status);
}
