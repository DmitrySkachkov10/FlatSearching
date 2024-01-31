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
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AdminAspect {
    private final LogService logService;

    public AdminAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* by.skachkovdmitry.personal_account.service.AdminService.*User*(..))")
    public void getUserMethods() {
    }

    @Pointcut("execution(* by.skachkovdmitry.personal_account.service.AdminService.update(..))")
    public void updateUser() {
    }

    @Pointcut("execution(* by.skachkovdmitry.personal_account.service.AdminService.create(..))")
    public void createUser() {
    }

    @Around("getUserMethods()")
    public Object getUserMethodsExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAndProceed(joinPoint, "Получение информации о пользователе (ях)");
    }

    @Around("updateUser()")
    public Object updateUserExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAndProceed(joinPoint, "Обновление полей пользователя");
    }

    @Around("createUser()")
    public Object createUserExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAndProceed(joinPoint, "Создание пользователя");
    }

    private Object logAndProceed(ProceedingJoinPoint joinPoint, String action) throws Throwable {
        Signature signature = joinPoint.getSignature();
        LogInfo logInfo = new LogInfo();
        logInfo.setEssenceType("ADMIN");
        logInfo.setUser((UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        logInfo.setId(signature.getName());

        try {
            Object result = joinPoint.proceed();
            logInfo.setText(action);
            send(logInfo);
            return result;
        } catch (Error e) {
            logInfo.setText("Ошибка в " + action + ": " + e.getMessage());
            send(logInfo);
            throw e;
        }
    }


    private void send(LogInfo logInfo) {
        try {
            logService.send(logInfo);
        }catch (RetryableException e) {
           log.error("Нет подключения к audit-service, данные не отправлены");
        }
    }
}
