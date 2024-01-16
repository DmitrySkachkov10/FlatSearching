package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IAdminService {
    void create(UserCreate userCreate);

    User getUser(UUID uuid);

    PageOfUser getUserList(int page, int size);

    void update(UserCreate userCreate, UUID uuid, long lastUpdate);
}
