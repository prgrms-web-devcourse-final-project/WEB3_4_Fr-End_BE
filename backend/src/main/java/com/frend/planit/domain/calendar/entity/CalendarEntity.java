package com.frend.planit.domain.calendar.entity;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
        return new CalendarEntity(
                requestDto.calendarTitle(),
                requestDto.startDate(),
                requestDto.endDate(),
                requestDto.time(),
                requestDto.alertTime(),
                requestDto.note()
        );
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

    private CalendarEntity(String calendarTitle, LocalDateTime startDate, LocalDateTime endDate,
                           LocalDateTime time, LocalDateTime alertTime, String note) {
        this.calendarTitle = calendarTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.alertTime = alertTime;
        this.note = note;
    }
}