package com.frend.planit.global.response;

import com.frend.planit.global.validation.CodeValidation;
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
    ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 본문의 값이 잘못되었습니다."),
    ATTRIBUTE_BINDING_ERROR(HttpStatus.BAD_REQUEST, "요청 파라미터의 값이 잘못되었습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "요청 파라미터의 값이 잘못되었습니다."),
    ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터의 타입이 잘못되었습니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "필수 경로 파라미터가 누락되었습니다."),
    JSON_NOT_READABLE(HttpStatus.BAD_REQUEST, "요청 본문의 형식이 잘못되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    COMMON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    /*********************************************************************************/

    private final int code;
    private final String message;

    ErrorType(HttpStatus status, String message) {
        CodeValidation.validateErrorCode(status);
        this.code = status.value();
        this.message = message;
    }

    ErrorType(int code, String message) {
        CodeValidation.validateErrorCode(code);
        this.code = code;
        this.message = message;
    }
}