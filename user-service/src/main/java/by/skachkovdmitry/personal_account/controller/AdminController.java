package by.skachkovdmitry.personal_account.controller;

import by.skachkovdmitry.personal_account.core.dto.PageOfUser;
import by.skachkovdmitry.personal_account.core.dto.UserCreate;
import by.skachkovdmitry.personal_account.service.api.IAdminService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserCreate userCreate) {
        adminService.create(userCreate);
        return new ResponseEntity<>("Пользователь создан", HttpStatusCode.valueOf(200));
    }


    @GetMapping
    public ResponseEntity<PageOfUser> getUsers(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(adminService.getUserList(page,size), HttpStatusCode.valueOf(200));
    }
}
