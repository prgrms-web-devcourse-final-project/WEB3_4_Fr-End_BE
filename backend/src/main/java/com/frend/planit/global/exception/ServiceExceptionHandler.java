package com.frend.planit.global.exception;

import com.frend.planit.global.response.ApiResponseHelper;
import com.frend.planit.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 * 서비스 관련 예외 처리를 위한 핸들러 클래스입니다.
 */
@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception) {
        return ApiResponseHelper.error(exception.getCode(),
                new ErrorResponse(exception.getMessage()));
    }
}