package by.skachkovdmitry.personal_account.aop;

import by.dmitryskachkov.entity.Error;
import by.skachkovdmitry.personal_account.core.dto.LogInfo;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.service.api.feign.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;


@Aspect
@Component
public class UserAspect {

    private final LogService logService;

    public UserAspect(LogService logService) {
        this.logService = logService;
    }

    @Around("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.logIn(..))")
    public Object login(ProceedingJoinPoint joinPoint) throws Throwable {
        return handleMethod(joinPoint, "Пользователь вошел в систему");
    }

    @Around("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.myInfo(..))")
    public Object userInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        return handleMethod(joinPoint, "Получение информации в профиле");
    }

    private Object handleMethod(ProceedingJoinPoint joinPoint, String action) throws Throwable {
        Signature signature = joinPoint.getSignature();
        LogInfo logInfo = new LogInfo();
        logInfo.setEssenceType("USER");
        logInfo.setId(signature.getName());
        logInfo.setUser(null);
        try {
            Object result = joinPoint.proceed();
            logInfo.setText(action);
            logInfo.setUser((UserSecurity) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal());
            logService.send(logInfo);
            return result;
        } catch (Error e) {
            logInfo.setText("Ошибка в " + action + ": " + e.getMessage());
            logService.send(logInfo);
            throw e;
        }
    }

}
