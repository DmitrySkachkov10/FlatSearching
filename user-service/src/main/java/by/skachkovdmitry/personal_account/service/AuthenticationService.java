package by.skachkovdmitry.personal_account.service;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.core.dto.verification.MailVerifyDTO;
import by.dmitryskachkov.entity.*;
import by.skachkovdmitry.personal_account.core.role.Roles;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.core.utils.JwtTokenHandler;
import by.skachkovdmitry.personal_account.repo.entity.MailVerifyEntity;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import by.skachkovdmitry.personal_account.service.api.IAuthenticationService;
import by.skachkovdmitry.personal_account.service.api.IMailService;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserService userService;
    private final IMailService mailService;

    private final JwtTokenHandler jwtHandler;

    private final String MAIL_REGEX = "\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}";

    private final String FIO_REGEX = "^[А-ЯЁA-Z][а-яёA-Za-z]+(\\s[А-ЯЁA-Z][а-яёA-Za-z]+){1,2}";


    private final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}";

    private final String COMMON_ERROR_MESSAGE = "Запрос содержит некорректные данные. Измените запрос и отправьте его ещё раз";


    public AuthenticationService(IUserService userService, IMailService mailService, JwtTokenHandler jwtHandler) {
        this.userService = userService;
        this.mailService = mailService;
        this.jwtHandler = jwtHandler;
    }

    @Override
    public String logIn(UserLogin userLogin) {
        UserEntity userEntity = userService.logIn(userLogin);
        User user = new User(userEntity.getUuid().toString(),
                userEntity.getDtCreate(),
                userEntity.getDtUpdate(),
                userEntity.getFio(),
                userEntity.getMail(),
                userEntity.getRole().toString(),
                userEntity.getStatus().toString());

        return jwtHandler.generateAccessToken(new UserSecurity(user.getUuid(),
                user.getMail(),
                user.getFio(),
                user.getRole()));
    }


    @Override
    @Transactional
    public void register(UserRegistration userRegistration) {
        isValidData(userRegistration);

        UserEntity userEntity = new UserEntity();
        userEntity.setMail(userRegistration.getMail());
        userEntity.setPassword(userRegistration.getPassword());
        userEntity.setFio(userRegistration.getFio());

        userEntity.setUuid(UUID.randomUUID());
        userEntity.setDtCreate(LocalDateTime.now());
        userEntity.setDtUpdate(userEntity.getDtCreate());
        userEntity.setRole(Roles.USER);
        userEntity.setStatus(Status.WAITING_ACTIVATION);

        userService.save(userEntity);
        mailService.create(userRegistration.getMail());
    }

    @Override
    public User myInfo() {
        UserSecurity userSecurity = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserEntity userEntity = userService.getUserByMail(userSecurity.getMail());

        return new User(userEntity.getUuid().toString(),
                userEntity.getDtCreate(),
                userEntity.getDtUpdate(),
                userEntity.getFio(),
                userEntity.getMail(),
                userEntity.getRole().toString(),
                userEntity.getStatus().toString());
    }

    @Override
    @Transactional
    public void verify(MailVerifyDTO mailVerifyDTO) {

        MailVerifyEntity mailVerifyEntity = new MailVerifyEntity();
        mailVerifyEntity.setCode(mailVerifyDTO.getCode());
        mailVerifyEntity.setMail(mailVerifyDTO.getMail());

        if (mailService.exists(mailVerifyEntity)) {
            userService.updateUserStatus(mailVerifyEntity.getMail(), Status.ACTIVATED);
        }

        //todo отдавать нормальным форматом все
    }


    private void isValidData(UserRegistration userRegistration) {

        Pattern mailPattern = Pattern.compile(MAIL_REGEX);
        Pattern fioPattern = Pattern.compile(FIO_REGEX);
        Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
        Errors<ValidationError> validationErrors = new Errors<>();

        if (!mailPattern.matcher(userRegistration.getMail()).matches()) {
            ValidationError validationError = new ValidationError("неверный формат почты", "email");
            validationErrors.add(validationError);
        }
        if (!passwordPattern.matcher(userRegistration.getPassword()).matches()) {
            ValidationError validationError = new ValidationError("неверный формат пароля", "password");
            validationErrors.add(validationError);
        }
        if (!fioPattern.matcher(userRegistration.getFio()).matches()) {
            ValidationError validationError = new ValidationError("неверный формат ФИО", "fio");
            validationErrors.add(validationError);
        }

        if (!validationErrors.getErrorList().isEmpty()) {
            throw new StructuredError(validationErrors);
        }
    }
}
