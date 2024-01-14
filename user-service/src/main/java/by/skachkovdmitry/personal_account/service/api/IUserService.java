package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.status.Status;


public interface IUserService {
    User getUserByMail(String mail);

    void save(UserRegistration userRegistration);

    void save(UserCreate userCreate);

    boolean exists(UserLogin userLogin);

    void updateUserStatus(String mail, Status status);
}
