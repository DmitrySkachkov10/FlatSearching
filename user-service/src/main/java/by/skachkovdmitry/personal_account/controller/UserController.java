package by.skachkovdmitry.personal_account.controller;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.core.dto.verification.MailVerifyDTO;
import by.skachkovdmitry.personal_account.core.utils.JwtTokenHandler;
import by.skachkovdmitry.personal_account.service.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    private final IAuthenticationService authenticationService;
    private final JwtTokenHandler jwtHandler;

    public UserController(IAuthenticationService authenticationService, JwtTokenHandler jwtHandler) {
        this.authenticationService = authenticationService;
        this.jwtHandler = jwtHandler;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistration userRegistration) {
        authenticationService.register(userRegistration);
        return new ResponseEntity<>("Пользователь зарегистрирован", HttpStatusCode.valueOf(201));
    }

    @GetMapping("/verification")
    public ResponseEntity<?> verify(@RequestParam String mail, @RequestParam String code) {
        authenticationService.verify(new MailVerifyDTO(code, mail));
        return new ResponseEntity<>("Пользователь верифицирован", HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin) {
        String token = authenticationService.logIn(userLogin);
        return new ResponseEntity<>(token, HttpStatus.ACCEPTED);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getInfo() {
        return new ResponseEntity<>(authenticationService.myInfo(), HttpStatus.ACCEPTED);
    }
}
