package by.skachkovdmitry.personal_account.service;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.dto.verification.MailVerifyDTO;
import by.skachkovdmitry.personal_account.core.exception.Errors;
import by.skachkovdmitry.personal_account.core.exception.StructuredError;
import by.skachkovdmitry.personal_account.core.exception.ValidationError;
import by.skachkovdmitry.personal_account.core.status.Status;
import by.skachkovdmitry.personal_account.repo.entity.MailVerifyEntity;
import by.skachkovdmitry.personal_account.repo.entity.UserEntity;
import by.skachkovdmitry.personal_account.service.api.IAuthenticationService;
import by.skachkovdmitry.personal_account.service.api.IMailService;
import by.skachkovdmitry.personal_account.service.api.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserService userService;
    private final IMailService mailService;

    private final String MAIL_REGEX = "\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}";

    private final String FIO_REGEX = "^[А-ЯЁ][а-яё]+(\\s[А-ЯЁ][а-яё]+){1,2}";

    private final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}";

    private final String COMMON_ERROR_MESSAGE = "Запрос содержит некорректные данные. Измените запрос и отправьте его ещё раз";


    public AuthenticationService(IUserService userService, IMailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public boolean logIn(UserLogin userLogin) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMail(userLogin.getMail());
        userEntity.setPassword(userLogin.getPassword());
        return userService.exists(userEntity);
    }

    @Transactional
    @Override
    public void register(UserRegistration userRegistration) {
        isValidData(userRegistration);

        UserEntity userEntity = new UserEntity();
        userEntity.setMail(userRegistration.getMail());
        userEntity.setPassword(userRegistration.getPassword());
        userEntity.setFio(userRegistration.getFio());
        userEntity.setPassword(userRegistration.getPassword());

        userEntity.setDtCreate(LocalDateTime.now());
        userEntity.setDtUpdate(userEntity.getDtCreate());

        userService.save(userEntity);
        mailService.create(userRegistration.getMail());
    }

    @Override
    public User myInfo() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
