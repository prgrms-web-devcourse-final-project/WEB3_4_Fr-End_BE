package com.frend.planit.domain.booking.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예약 전체 조회 시 상태별로 분리된 응답을 담는 DTO입니다.
 * <p>- upcoming: 예정된 예약 - pastOrCanceled: 지난 예약 or 취소된 예약</p>
 */

@Getter
@AllArgsConstructor
public class BookingListResponseDto {

    private List<BookingSummaryDto> upcoming;
    private List<BookingSummaryDto> pastOrCanceled;
    
}
