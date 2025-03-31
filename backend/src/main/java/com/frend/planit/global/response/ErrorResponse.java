package com.frend.planit.global.response;

import java.util.List;
import lombok.Getter;

/*
 * 에러 응답을 위한 DTO 클래스입니다.
 * 확장성을 위해 별도의 DTO로 작성하였습니다.
 */
@Getter
public class ErrorResponse {

    private final String message;
    private final List<Detail> details;

    public ErrorResponse(String message) {
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(String message, List<Detail> details) {
        this.message = message;
        this.details = details;
    }

    @Getter
    public static class Detail {

        private final String field;
        private final Object value;
        private final String message;

        public Detail(String field, String message) {
            this.field = field;
            this.value = null;
            this.message = message;
        }

        public Detail(String field, Object value, String message) {
            this.field = field;
            this.value = value;
            this.message = message;
        }
    }
}