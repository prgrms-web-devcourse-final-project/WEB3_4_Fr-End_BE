package com.frend.planit.global.exception.handler;

import com.frend.planit.global.response.ApiResponseHelper;
import com.frend.planit.global.response.ErrorResponse;
import com.frend.planit.global.response.ErrorResponse.Detail;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/*
 * 공통 예외 처리를 위한 핸들러 클래스입니다.
 */
@Order(3)
@RestControllerAdvice
public class CommonExceptionHandler {

    // HTTP 메소드의 지원 예외를 처리합니다.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return ApiResponseHelper.error(ErrorType.METHOD_NOT_ALLOWED.getCode(),
                new ErrorResponse(ErrorType.METHOD_NOT_ALLOWED.getMessage()));
    }

    // 존재하지 않는 경로의 예외를 처리합니다.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException exception) {
        Detail detail = new Detail(
                "path",
                exception.getResourcePath(),
                "해당 경로가 존재하지 않습니다."
        );

        return ApiResponseHelper.error(ErrorType.NO_RESOURCE_FOUND.getCode(),
                new ErrorResponse(ErrorType.NO_RESOURCE_FOUND.getMessage(), List.of(detail)));
    }

    // 나머지 예외를 처리합니다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        Detail detail = new Detail(
                "cause",
                exception.getClass().getName(),
                exception.getMessage());

        return ApiResponseHelper.error(ErrorType.COMMON_SERVER_ERROR.getCode(),
                new ErrorResponse(ErrorType.COMMON_SERVER_ERROR.getMessage(), List.of(detail)));
    }
}