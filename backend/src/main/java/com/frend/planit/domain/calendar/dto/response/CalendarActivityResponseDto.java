// com.frend.planit.domain.calendar.dto.response.CalendarActivityResponseDto.java
package com.frend.planit.domain.calendar.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarActivityResponseDto {

    private Long calendarId;
    private String calendarTitle;
    private String note;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private List<String> sharedUserNicknames; // 공유자 닉네임 목록
}
