package com.frend.planit.global.response;

import com.frend.planit.global.validation.CodeValidation;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/*
 * Api 응답을 위한 클래스입니다.
 * 클래스 객체는 생성하지 않아도 됩니다.
 */
@UtilityClass
public class ApiResponse<T> {

    public <T> ResponseEntity<T> success(@NonNull HttpStatus successStatus) {
        CodeValidation.validateSuccessCode(successStatus);
        return ResponseEntity.status(successStatus).build();
    }

    public <T> ResponseEntity<T> success(@NonNull HttpStatus successStatus, @NonNull T data) {
        CodeValidation.validateSuccessCode(successStatus);
        return ResponseEntity.status(successStatus).body(data);
    }

    public <T> ResponseEntity<T> success(int successCode) {
        CodeValidation.validateSuccessCode(successCode);
        return ResponseEntity.status(successCode).build();
    }

    public <T> ResponseEntity<T> success(int successCode, @NonNull T data) {
        CodeValidation.validateSuccessCode(successCode);
        return ResponseEntity.status(successCode).body(data);
    }
}