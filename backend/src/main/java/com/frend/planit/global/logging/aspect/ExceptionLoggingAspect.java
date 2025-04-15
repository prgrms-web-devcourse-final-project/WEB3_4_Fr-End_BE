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
 * 예외 응답을 로깅하는 AOP 클래스입니다.
 * ExceptionHandler 어노테이션이 붙은 메서드를 대상으로 합니다.
 */
@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    private final HttpServletRequest request;

    public ExceptionLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void exceptionHandlerMethods() {
    }

    @Around("exceptionHandlerMethods()")
    public Object logExceptionHandling(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Throwable ex = (Throwable) args[0];

        LogBuilder logBuilder = new LogBuilder(request, joinPoint)
                .error()
                .appendMethodName()
                .proceed()
                .appendStatusCode()
                .appendResponseBody()
                .appendRequestParameter();

        log.error(logBuilder.appendExecutionTime().appendClientIP().toString());
        log.debug("원인: ", ex);

        return logBuilder.getResult();
    }
}