package com.frend.planit.global.exception;

import com.frend.planit.global.response.ErrorType;
import lombok.Getter;

/*
 * 서비스 관련 예외 클래스입니다.
 * ErrorType 열거형을 통해서만 예외를 생성할 수 있습니다.
 */
@Getter
public class ServiceException extends RuntimeException {

    private final int code;

    public ServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }

    public ServiceException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.code = errorType.getCode();
    }
}