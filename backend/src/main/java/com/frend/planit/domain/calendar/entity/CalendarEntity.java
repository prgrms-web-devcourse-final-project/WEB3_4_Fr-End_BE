package com.frend.planit.domain.calendar.entity;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import com.frend.planit.global.response.ErrorType;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "calendar_title", nullable = false, length = 20)
    private String calendarTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "alert_time")
    private LocalDateTime alertTime;

    @Column(name = "note", length = 200)
    private String note;

    public static CalendarEntity fromDto(CalendarRequestDto requestDto, User user) {
        validateDates(requestDto.startDate(), requestDto.endDate(), requestDto.alertTime());
        return CalendarEntity.builder()
                .user(user)
                .calendarTitle(requestDto.calendarTitle())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .alertTime(requestDto.alertTime())
                .note(requestDto.note())
                .build();
    }

    public void updateCalendar(CalendarRequestDto requestDto) {
        validateDates(requestDto.startDate(), requestDto.endDate(), requestDto.alertTime());
        this.calendarTitle = requestDto.calendarTitle();
        this.startDate = requestDto.startDate();
        this.endDate = requestDto.endDate();
        this.alertTime = requestDto.alertTime();
        this.note = requestDto.note();
    }

    private static void validateDates(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alertTime) {
        if (endDate.isBefore(startDate)) {
            throw ErrorType.INVALID_CALENDAR_DATE.serviceException();
        }
        if (alertTime != null && alertTime.isAfter(startDate)) {
            throw ErrorType.INVALID_ALERT_TIME.serviceException();
        }
    }
}