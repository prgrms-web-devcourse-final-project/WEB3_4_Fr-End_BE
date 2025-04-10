package com.frend.planit.domain.calendar.dto.response;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CalendarResponseDto {

    @Schema(description = "캘린더 ID")
    private final Long id;

    @Schema(description = "캘린더 제목")
    private final String calendarTitle;

    @Schema(description = "시작 날짜 및 시간")
    private final LocalDateTime startDate;

    @Schema(description = "종료 날짜 및 시간")
    private final LocalDateTime endDate;

    @Schema(description = "알림 시간")
    private final LocalDateTime alertTime;

    @Schema(description = "캘린더 설명 또는 메모")
    private final String note;

    @Schema(description = "캘린더 색상 코드")
    private final String labelColor;

    @Schema(description = "생성일")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일")
    private final LocalDateTime modifiedAt;

    @Schema(description = "소유자 유저 ID")
    private final Long userId;

    public CalendarResponseDto(CalendarEntity entity) {
        this.id = entity.getId();
        this.calendarTitle = entity.getCalendarTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.alertTime = entity.getAlertTime();
        this.note = entity.getNote();
        this.labelColor = entity.getLabelColor();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.userId = entity.getUser().getId();
    }
}