package com.frend.planit.global.logging.aspect;

import com.frend.planit.global.logging.LogBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/*
 * 컨트롤러의 응답을 로깅하는 AOP 클래스입니다.
 * RestController 어노테이션이 붙은 클래스의 메서드를 대상으로 합니다.
 */
@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    private final HttpServletRequest request;

    public ControllerLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        LogBuilder logBuilder = new LogBuilder(request, joinPoint)
                .success()
                .appendMethodName()
                .proceed()
                .appendStatusCode()
                .appendResponseBody()
                .appendRequestParameter();

        log.info(logBuilder.appendExecutionTime().appendClientIP().toString());

        return logBuilder.getResult();
    }
}