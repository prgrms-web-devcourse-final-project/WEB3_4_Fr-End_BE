package com.frend.planit.global.response;

import lombok.Getter;
import lombok.NonNull;

/*
 * 에러 응답을 위한 DTO 클래스입니다.
 * 확장성을 위해 별도의 DTO로 작성하였습니다.
 */
@Getter
public class ErrorResponse {

    private final String message;

    public ErrorResponse(@NonNull String message) {
        this.message = message;
    }
}