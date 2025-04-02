package com.frend.planit.global.exception.handler;

import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * 서비스 관련 예외 처리를 위한 핸들러 클래스입니다.
 */
@Order(1)
@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception) {
        return ResponseEntity
                .status(exception.getCode())
                .body(new ErrorResponse(exception.getMessage()));
    }
}