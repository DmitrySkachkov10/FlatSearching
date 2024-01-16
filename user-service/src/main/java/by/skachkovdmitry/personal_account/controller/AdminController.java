package by.skachkovdmitry.personal_account.controller;

import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.core.utils.JwtTokenHandler;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import by.skachkovdmitry.personal_account.service.api.IAuthenticationService;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        return new ResponseEntity<>("Пользователь создан", HttpStatusCode.valueOf(200));
    }


    @GetMapping
    public ResponseEntity<PageOfUser> getUsers(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(adminService.getUserList(page, size), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable UUID uuid) {
        return new ResponseEntity<>(adminService.getUser(uuid), HttpStatusCode.valueOf(200));
    }


    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<String> updateUser(@PathVariable UUID uuid,
                                             @PathVariable long dt_update,
                                             @RequestBody UserCreate userCreate) {


        adminService.update(userCreate, uuid, dt_update);
        return new ResponseEntity<>("Обновлен", HttpStatusCode.valueOf(200));
    }
}
