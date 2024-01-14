package by.skachkovdmitry.personal_account.controller;

import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    public ResponseEntity<String> create(@RequestBody UserCreate userCreate) {
        adminService.create(userCreate);
        return new ResponseEntity<>("Пользователь создан", HttpStatusCode.valueOf(200));
    }
}
