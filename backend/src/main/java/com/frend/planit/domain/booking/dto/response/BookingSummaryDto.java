package com.frend.planit.domain.booking.dto.response;

import com.frend.planit.domain.booking.entity.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 예약 목록 조회, 목록 응답 시에 사용하는 Response DTO입니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-11
 */
@Getter
@Builder
@AllArgsConstructor
public class BookingSummaryDto {

    /**
     * 예약 ID
     */
    private final Long bookingId;
    /**
     * 숙소명
     */
    private final String accommodationName;
    /**
     * 숙소 주소
     */
    private final String accommodationAddress;
    /**
     * 이미지 URL
     */
    private final String accommodationImage;
    /**
     * 체크인 날짜
     */
    private final LocalDate checkInDate;
    /**
     * 체크인 시간
     */
    private final LocalTime checkInTime;
    /**
     * 예약 상태 enum
     */
    private final BookingStatus bookingStatus;
    /**
     * 결제 상태
     */
    private final String paymentStatus;
    /**
     * 예약 생성일시
     */
    private final LocalDateTime reservedAt;
    /**
     * 예약 취소 가능 여부
     */
    private final Boolean canCancel;

}
