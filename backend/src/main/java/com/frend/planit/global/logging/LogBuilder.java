package com.frend.planit.global.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * 메서드 체이닝으로 로그를 생성하는 클래스입니다.
 */
public class LogBuilder {

    private final static String SEPARATOR = " | ";
    private final static String SUCCESS_MESSAGE = "[API 호출 성공]";
    private final static String ERROR_MESSAGE = "[API 호출 실패]";
    private final static String METHOD_NAME = "호출 메서드: ";
    private final static String STATUS_CODE = "상태 코드: ";
    private final static String RESPONSE_BODY = "응답 본문: ";
    private final static String REQUEST_PARAMETER = "요청 파라미터: ";
    private final static String EXECUTION_TIME = "실행 시간: ";
    private final static String CLIENT_IP = "사용자 IP: ";

    private final HttpServletRequest request;
    private final StringBuilder stringBuilder;
    private final ObjectMapper objectMapper;
    private final JoinPoint joinPoint;
    private Object proceedResult;
    private Long executionTime;

    public LogBuilder(HttpServletRequest request, JoinPoint joinPoint) {
        this.request = request;
        stringBuilder = new StringBuilder();
        objectMapper = new ObjectMapper();
        this.joinPoint = joinPoint;
        proceedResult = null;
        executionTime = null;
    }

    public LogBuilder success() {
        append(SUCCESS_MESSAGE);
        return this;
    }

    public LogBuilder error() {
        append(ERROR_MESSAGE);
        return this;
    }

    public LogBuilder appendMethodName() {
        String methodName = joinPoint.getSignature().toShortString();
        append(METHOD_NAME + methodName);
        return this;
    }

    public LogBuilder proceed() throws Throwable {
        if (Objects.isNull(proceedResult) && joinPoint instanceof ProceedingJoinPoint _joinPoint) {
            long start = System.nanoTime();
            proceedResult = _joinPoint.proceed();
            executionTime = (System.nanoTime() - start) / 1_000_000;
        }
        return this;
    }

    public LogBuilder appendStatusCode() {
        if (proceedResult instanceof ResponseEntity<?> entity) {
            String statusCode = entity.getStatusCode().toString();
            append(STATUS_CODE + statusCode);
        }
        return this;
    }

    public LogBuilder appendResponseBody() {
        if (proceedResult instanceof ResponseEntity<?> entity) {
            String responseBody = null;
            try {
                responseBody = objectMapper.writeValueAsString(entity.getBody());
            } catch (JsonProcessingException e) {
                if (Objects.isNull(entity.getBody())) {
                    responseBody = "파싱 실패";
                }
            }
            append(RESPONSE_BODY + responseBody);
        }
        return this;
    }

    public LogBuilder appendRequestParameter() {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        StringBuilder _stringBuilder = new StringBuilder();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestBody || annotation instanceof ModelAttribute ||
                        annotation instanceof RequestParam || annotation instanceof PathVariable) {
                    if (!_stringBuilder.isEmpty()) {
                        _stringBuilder.append(", ");
                    }
                    _stringBuilder.append(args[i]);
                }
            }
        }

        if (!_stringBuilder.isEmpty()) {
            append(REQUEST_PARAMETER + "{" + _stringBuilder + "}");
        }

        return this;
    }

    public LogBuilder appendExecutionTime() throws Throwable {
        if (!Objects.isNull(executionTime)) {
            append(EXECUTION_TIME + executionTime + "ms");
        }
        return this;
    }

    public LogBuilder appendClientIP() {
        String clientIP = request.getRemoteAddr();
        String maskedIP;
        if (clientIP.contains(".")) {
            // IPv4 마스킹: 앞 두 옥텟만 표시
            maskedIP = clientIP.replaceAll("^(\\d+\\.\\d+)\\.\\d+\\.\\d+$", "$1.*.*");
        } else if (clientIP.contains(":")) {
            // IPv6 마스킹: 앞 2개 그룹만 표시
            maskedIP = clientIP.replaceAll("^(([0-9a-fA-F]{0,4}:){1,2}).*", "$1::");
        } else {
            maskedIP = clientIP;
        }
        append(CLIENT_IP + maskedIP);
        return this;
    }

    public String toString() {
        return stringBuilder.toString();
    }

    public Object getResult() {
        return proceedResult;
    }

    private void append(String text) {
        if (!stringBuilder.isEmpty()) {
            stringBuilder.append(SEPARATOR);
        }
        stringBuilder.append(text);
    }
}