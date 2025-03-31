package com.frend.planit.domain.calendar.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CalendarRequestDto {
    private String calendarTitle;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime time;
    private LocalDateTime alertTime;
    private String note;
}