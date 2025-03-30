package com.frend.planit.global.exception.handler;

import com.frend.planit.global.response.ApiResponseHelper;
import com.frend.planit.global.response.ErrorResponse;
import com.frend.planit.global.response.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * 공통 예외 처리를 위한 핸들러 클래스입니다.
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ApiResponseHelper.error(ErrorType.COMMON_SERVER_ERROR.getCode(),
                new ErrorResponse(ErrorType.COMMON_SERVER_ERROR.getMessage()));
    }
}