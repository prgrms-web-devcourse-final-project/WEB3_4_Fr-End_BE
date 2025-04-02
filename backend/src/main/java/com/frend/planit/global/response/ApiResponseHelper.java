package com.frend.planit.global.response;

import java.nio.charset.StandardCharsets;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/*
 * Api 응답을 위한 헬퍼 클래스입니다.
 * 클래스 객체는 생성하지 않아도 됩니다.
 */
@UtilityClass
public class ApiResponseHelper<T> {

    /*
     * 성공 응답을 반환합니다.
     */
    public <T> ResponseEntity<T> success(@NonNull HttpStatus status) {
        return ResponseEntity.status(status)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .build();
    }

    public <T> ResponseEntity<T> success(@NonNull HttpStatus status, @NonNull T data) {
        return ResponseEntity.status(status)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }

    public <T> ResponseEntity<T> success(int code) {
        return ResponseEntity.status(code)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .build();
    }

    public <T> ResponseEntity<T> success(int code, @NonNull T data) {
        return ResponseEntity.status(code)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }

    /*
     * 에러 응답을 반환합니다.
     * 에러는 핸들러에 의해 자동 처리되므로 특별한 경우가 아니면 사용을 지양해주세요.
     */
    public <T> ResponseEntity<T> error(@NonNull HttpStatus status, @NonNull T data) {
        return ResponseEntity.status(status)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }

    public <T> ResponseEntity<T> error(@NonNull int code, @NonNull T data) {
        return ResponseEntity.status(code)
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                .body(data);
    }
}