package by.skachkovdmitry.personal_account.aop;

import by.dmitryskachkov.entity.Error;
import by.skachkovdmitry.personal_account.core.dto.LogInfo;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.service.api.feign.LogService;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
@Slf4j
public class UserAspect {

    private final LogService logService;

    public UserAspect(LogService logService) {
        this.logService = logService;
    }

    @Around("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.logIn(..))")
    public Object login(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        LogInfo logInfo = new LogInfo();
        logInfo.setEssenceType("USER");

        Object result = joinPoint.proceed();
        UserSecurity userSecurity = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        logInfo.setText("Пользователь вошел в систему");
        logInfo.setId(userSecurity.getUuid());
        logInfo.setUser(userSecurity);
        send(logInfo);
        return result;
    }

    @Around("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.myInfo(..))")
    public Object userInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        LogInfo logInfo = new LogInfo();
        logInfo.setEssenceType("USER");
        logInfo.setId(signature.getName());
        try {
            Object result = joinPoint.proceed();
            logInfo.setText("Получение информации в профиле");
            UserSecurity userSecurity = (UserSecurity) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            logInfo.setUser(userSecurity);
            logInfo.setId(userSecurity.getUuid());
            send(logInfo);
            return result;
        } catch (Error e) {
            logInfo.setText("Ошибка в " + "Получение информации в профиле" + ": " + e.getMessage());
            send(logInfo);
            throw e;
        }
    }


    private void send(LogInfo logInfo) {
        try {
            if (logInfo.getUser() == null) {
                return;
            }
            logService.send(logInfo);
        } catch (RetryableException e) {
            log.error("Нет подключения к audit-service, данные не отправлены");
        }
    }

}
