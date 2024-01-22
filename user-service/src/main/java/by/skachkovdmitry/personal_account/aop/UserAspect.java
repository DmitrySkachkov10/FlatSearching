package by.skachkovdmitry.personal_account.aop;

import by.dmitryskachkov.dto.ErrorDto;
import by.dmitryskachkov.entity.*;
import by.dmitryskachkov.entity.Error;
import by.skachkovdmitry.personal_account.core.dto.LogInfo;
import by.skachkovdmitry.personal_account.core.dto.security.UserSecurity;
import by.skachkovdmitry.personal_account.service.api.feign.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class UserAspect {

    private final LogService logService;

    public UserAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.*(..))")
    private void beforeAuthorizationMethods() {
    }

    @Pointcut("execution(* by.skachkovdmitry.personal_account.service.AuthenticationService.myInfo(..))")
    private void myInfoMethod() {
    }

    @Around("beforeAuthorizationMethods() && !myInfoMethod()")
    public Object handleBeforeAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        return handleMethod(joinPoint, signature, null);
    }

    @Around("myInfoMethod()")
    public Object handleAfterAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        UserSecurity user = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return handleMethod(joinPoint, signature, user);
    }

    private Object handleMethod(ProceedingJoinPoint joinPoint, Signature signature, UserSecurity user) throws Throwable {
        LogInfo logInfo = new LogInfo();
        logInfo.setEssenceType("USER");
        logInfo.setUser(user);

        try {
            Object object = joinPoint.proceed();
            logInfo.setId(signature.getDeclaringTypeName());
            logInfo.setText(signature.getName().toString());
            System.out.println(logInfo.getText() + " in " + logInfo.getId());
            logService.send(logInfo);
            return object;

        } catch (Error e) {
            if (e instanceof StructuredError) {
                logInfo.setId(signature.getDeclaringTypeName());
                String result = ((StructuredError)e).getErrors().getErrorList().stream()
                        .map(er -> "Field: " + ((Error) er).getField())
                        .collect(Collectors.joining(", ")).toString();
                logInfo.setText(result);
                System.out.println(logInfo.getText() + " in " + logInfo.getId());
                logService.send(logInfo);
            } else {
                logInfo.setId(signature.getDeclaringTypeName());
                logInfo.setText(signature.getName());
                System.out.println(logInfo.getText() + " in " + logInfo.getId());
                logService.send(logInfo);
            }
            throw e;
        }
    }
}
