package com.frend.planit.global.response;

import com.frend.planit.global.exception.ServiceException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * 에러 코드와 메세지를 정의하는 열거형입니다.
 * 에러 정보를 한곳에서 관리할 수 있도록 하였습니다.
 */
@Getter
public enum ErrorType {

    /*
     * 필요에 따라 도메인 별로 구분하여 추가해주세요.
     * 에러 코드는 HttpStatus 열거값으로 표준 코드만 지정할 수 있습니다.
     */
    /*********************************************************************************/

    // User
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    UNSUPPORTED_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 타입입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    
    // Global
    REQUEST_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.INTERNAL_SERVER_ERROR, "경로 파라미터가 누락되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 경로를 찾을 수 없습니다."),
    COMMON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

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

    public ServiceException serviceException() {
        return new ServiceException(this);
    }

    public ServiceException serviceException(Throwable cause) {
        return new ServiceException(this, cause);
    }
}