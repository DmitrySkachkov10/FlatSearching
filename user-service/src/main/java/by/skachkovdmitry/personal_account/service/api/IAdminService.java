package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;

import java.util.List;

public interface IAdminService {
    void create(UserCreate userCreate);

    User getUser(String uuid);

    PageOfUser getUserList(int page, int size);

    void update(User user);
}
