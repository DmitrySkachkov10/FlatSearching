package by.skachkovdmitry.personal_account.service.api;

import by.skachkovdmitry.personal_account.core.dto.User;
import by.skachkovdmitry.personal_account.core.dto.UserLogin;
import by.skachkovdmitry.personal_account.core.dto.UserRegistration;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.core.dto.verification.MailVerifyDTO;

public interface IAuthenticationService {
    String logIn(UserLogin userLogin);

    void register(UserRegistration userRegistration) ;

    User myInfo();

    void verify(MailVerifyDTO mailVerifyDTO);
}
