package by.skachkovdmitry.personal_account.controller;

import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import by.skachkovdmitry.personal_account.service.api.IAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class AdminController {

    private final IAdminService adminService;

    private final IAuthenticationService authenticationService;

    public AdminController(IAdminService adminService, IAuthenticationService authenticationService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserCreate userCreate) {
        adminService.create(userCreate);
        return new ResponseEntity<>("Пользователь создан", HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<PageOfUser> getUsers(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(adminService.getUserList(page, size), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable UUID uuid) {
        return new ResponseEntity<>(adminService.getUser(uuid),  HttpStatus.OK);
    }


    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<String> updateUser(@PathVariable UUID uuid,
                                             @PathVariable long dt_update,
                                             @RequestBody UserCreate userCreate) {


        adminService.update(userCreate, uuid, dt_update);
        return new ResponseEntity<>("Обновлен",  HttpStatus.OK);
    }
}
