package com.frend.planit.global.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/*
 * Api 응답을 위한 헬퍼 클래스입니다.
 * 클래스 객체는 생성하지 않아도 됩니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private static final String WRONG_SUCCESS_CODE = "잘못된 응답 코드입니다.";

    public static <T> ResponseEntity<T> success(HttpStatus successCode) {
        checkSuccessCode(successCode);
        return ResponseEntity.status(successCode).build();
    }

    public static <T> ResponseEntity<T> success(HttpStatus successCode, T data) {
        checkSuccessCode(successCode);
        return ResponseEntity.status(successCode).body(data);
    }

    public static <T> ResponseEntity<T> success(int successCode) {
        checkSuccessCode(successCode);
        return ResponseEntity.status(successCode).build();
    }

    public static <T> ResponseEntity<T> success(int successCode, T data) {
        checkSuccessCode(successCode);
        return ResponseEntity.status(successCode).body(data);
    }

    private static void checkSuccessCode(HttpStatus successCode) {
        if (!successCode.is2xxSuccessful()) {
            throw new IllegalArgumentException(WRONG_SUCCESS_CODE);
        }
    }

    private static void checkSuccessCode(int successCode) {
        if (successCode < 200 || successCode >= 300) {
            throw new IllegalArgumentException(WRONG_SUCCESS_CODE);
        }
    }
}