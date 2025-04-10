package com.frend.planit.domain.booking.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>예약 상태를 정의하는 열거형입니다.</p>
 * 프론트엔드에서 한글 상태 값을 받아 해당 열거형 상수로 변환합니다.
 *
 * @author zelly
 * @since 2025-04-10
 */
public enum BookingStatus {
    RESERVED("예약 완료"),
    CANCELED("예약 취소"),
    PENDING("예약 확정 대기"),
    COMPLETED("이용 완료");

    private final String koreanStatus;

    BookingStatus(String koreanStatus) {
        this.koreanStatus = koreanStatus;
    }

    @JsonValue
    public String getKoreanStatus() {
        return koreanStatus;
    }

    //  미리 Map 캐싱
    private static final Map<String, BookingStatus> KOREAN_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(
                    BookingStatus::getKoreanStatus,
                    status -> status
            ));

    @JsonCreator
    public static BookingStatus fromKoreanStatus(String value) {
        BookingStatus result = KOREAN_MAP.get(value);
        if (result == null) {
            throw new IllegalArgumentException("Unknown status: " + value);
        }
        return result;
    }
}