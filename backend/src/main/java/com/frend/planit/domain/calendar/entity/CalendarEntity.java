package com.frend.planit.domain.calendar.entity;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CalendarEntity extends BaseTime {

    @Column(name = "calendar_title", nullable = false, length = 20)
    private String calendarTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "alert_time")
    private LocalDateTime alertTime;

    @Column(name = "note", length = 200)
    private String note;

    // DTO에서 Entity 변환 메서드
    public static CalendarEntity fromDto(CalendarRequestDto requestDto) {
        return CalendarEntity.builder()
                .calendarTitle(requestDto.calendarTitle())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .time(requestDto.time())
                .alertTime(requestDto.alertTime())
                .note(requestDto.note())
                .build();
    }

    // 업데이트 메서드
    public void updateCalendar(CalendarRequestDto requestDto) {
        this.calendarTitle = requestDto.calendarTitle();
        this.startDate = requestDto.startDate();
        this.endDate = requestDto.endDate();
        this.time = requestDto.time();
        this.alertTime = requestDto.alertTime();
        this.note = requestDto.note();
    }
}
