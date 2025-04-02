package com.frend.planit.global.validation;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

/*
 * 코드 유효성 검사를 위한 클래스입니다.
 */
@UtilityClass
public class CodeValidation {

    private final String WRONG_SUCCESS_CODE = "잘못된 성공 코드입니다.";
    private final String WRONG_FAILURE_CODE = "잘못된 실패 코드입니다.";

    public void validateSuccessCode(@NonNull HttpStatus successCode) {
        if (!successCode.is2xxSuccessful()) {
            throw new IllegalArgumentException(WRONG_SUCCESS_CODE);
        }
    }

    public void validateSuccessCode(int successCode) {
        if (successCode < 200 || 300 <= successCode) {
            throw new IllegalArgumentException(WRONG_SUCCESS_CODE);
        }
    }

    public void validateFailureCode(@NonNull HttpStatus failureCode) {
        if (failureCode.is2xxSuccessful()) {
            throw new IllegalArgumentException(WRONG_FAILURE_CODE);
        }
    }

    public void validateFailureCode(int failureCode) {
        if (200 <= failureCode && failureCode < 300) {
            throw new IllegalArgumentException(WRONG_FAILURE_CODE);
        }
    }
}