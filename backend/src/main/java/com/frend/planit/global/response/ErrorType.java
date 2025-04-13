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

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // User
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    UNSUPPORTED_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 타입입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),

    // Image
    MIME_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."),
    IMAGE_CLEAN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 정리 작업에 실패했습니다."),

    // Global
    REQUEST_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.INTERNAL_SERVER_ERROR, "경로 파라미터가 누락되었습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 경로를 찾을 수 없습니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    COMMON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // Accommodation
    ACCOMMODATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 숙소를 찾을 수 없습니다."),
    ACCOMMODATION_DELETE_UNAUTHORIZED(HttpStatus.FORBIDDEN, "관리자만 숙소를 삭제할 수 있습니다."),

    // MateBoard
    MATE_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다."),
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // MateComment
    MATE_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),

    // MateApplication
    SELF_APPLICATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "자신의 게시글에는 신청할 수 없습니다."),
    MATE_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이미 모집이 종료된 게시글입니다."),
    MATE_ALREADY_FULL(HttpStatus.BAD_REQUEST, "모집 인원이 초과되었습니다."),
    ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 신청한 게시글입니다."),
    MATE_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "신청 내역이 존재하지 않습니다."),
    INVALID_CANCEL_STATUS(HttpStatus.BAD_REQUEST, "대기 상태일 때만 신청을 취소할 수 있습니다."),

    // Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스케줄이 존재하지 않습니다."),
    SCHEDULE_DAY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스케줄 날짜가 존재하지 않습니다."),

    // Travel
    TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 행선지가 존재하지 않습니다."),

    // Calendar
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 캘린더가 존재하지 않습니다."),
    INVALID_CALENDAR_DATE(HttpStatus.BAD_REQUEST, "종료 시간은 시작 시간보다 빠를 수 없습니다."),
    INVALID_ALERT_TIME(HttpStatus.BAD_REQUEST, "알람 시간은 시작 시간보다 늦을 수 없습니다."),

    // LIKE
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요가 눌려 있습니다."),
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "좋아요가 존재하지 않습니다"),

    // AI Chat
    AI_CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방이 존재하지 않습니다."),

    // Invite
    INVITE_NOT_FOUND(HttpStatus.NOT_FOUND, "초대 정보를 찾을 수 없습니다."),
    INVITE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 코드입니다."),

    // Booking
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),
    BOOKING_CANNOT_DELETE(HttpStatus.BAD_REQUEST, "해당 예약을 삭제할 수 없습니다."),
    INVALID_BOOKING_CANCEL(HttpStatus.BAD_REQUEST, "해당 예약은 취소할 수 없습니다."),
    IAMPORT_TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "아임포트 액세스 토큰 발급 실패"),
    IAMPORT_CANCEL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 취소 요청 실패");

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