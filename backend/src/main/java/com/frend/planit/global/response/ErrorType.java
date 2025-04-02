package com.frend.planit.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * 에러 코드와 메세지를 정의하는 열거형입니다.
 * 에러 정보를 한곳에서 관리할 수 있도록 하였습니다.
 */
@Getter
public enum ErrorType {

    /*
     * 아래는 예시입니다.
     * 필요에 따라 도메인 별로 구분하여 추가해주세요.
     * 에러 코드는 HttpStatus 또는 숫자로 지정할 수 있습니다.
     * 에러 코드이므로 2xx 범위의 코드는 지정할 수 없습니다.
     */
    EXAMPLE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    EXAMPLE_CUSTOM_ERROR(444, "오류가 발생했습니다."),

    /*********************************************************************************/

    // Global
    ARGUMENT_BINDING_ERROR(HttpStatus.BAD_REQUEST, "요청 본문의 값이 잘못되었습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "요청 파라미터의 값이 잘못되었습니다."),
    ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터의 타입이 잘못되었습니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.INTERNAL_SERVER_ERROR, "경로 파라미터가 누락되었습니다."),
    JSON_NOT_READABLE(HttpStatus.BAD_REQUEST, "요청 본문의 형식이 잘못되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 경로를 찾을 수 없습니다."),
    COMMON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    GLOBAL_TEST_CODE(444, "테스트 컨트롤러의 코드입니다."),

    // MateBoard
    MATE_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다."),

    // MateComment
    MATE_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다.");


    /*********************************************************************************/

    private final int code;
    private final String message;

    ErrorType(HttpStatus status, String message) {
        this.code = status.value();
        this.message = message;
    }

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}