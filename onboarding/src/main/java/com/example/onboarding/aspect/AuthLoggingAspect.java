package com.example.onboarding.aspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.onboarding.dto.AuthRequest;

@Slf4j
@Aspect
@Component
public class AuthLoggingAspect {

    // Log all methods inside AuthController
    @Around("execution(* com.example.onboarding.controller.AuthController.*(..))")
    public Object logAuthMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Auth method called: {}", joinPoint.getSignature());

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof AuthRequest request) {
                log.info("Login attempt for user: {}", request.getUsername());
            }
        }

        try {
            Object result = joinPoint.proceed();
            log.info("Auth method success: {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.error("Auth method failed: {}", e.getMessage());
            throw e;
        }
    }
}
