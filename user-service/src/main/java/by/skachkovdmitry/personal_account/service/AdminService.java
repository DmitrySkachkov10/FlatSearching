package by.skachkovdmitry.personal_account.service;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    private final IUserService userService;

    public AdminService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void create(UserCreate userCreate) {
        userService.save(userCreate);
    }

    @Override
    public User getUser(String uuid) {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public void update(User user) {

    }
}
