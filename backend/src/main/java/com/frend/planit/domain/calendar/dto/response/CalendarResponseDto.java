package com.frend.planit.domain.calendar.dto.response;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CalendarResponseDto {
    private final Long id;
    private final String calendarTitle;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime time;
    private final LocalDateTime alertTime;
    private final String note;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CalendarResponseDto(CalendarEntity entity) {
        this.id = entity.getId();
        this.calendarTitle = entity.getCalendarTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.time = entity.getTime();
        this.alertTime = entity.getAlertTime();
        this.note = entity.getNote();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
    }
}