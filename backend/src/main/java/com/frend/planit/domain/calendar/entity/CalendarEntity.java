package com.frend.planit.domain.calendar.entity;

import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
@Getter
@NoArgsConstructor
public class CalendarEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "calendar_title", nullable = false, length = 100)
    private String calendarTitle;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "alert_time")
    private LocalDateTime alertTime;

    @Column(name = "note", length = 200)
    private String note;

    public CalendarEntity(String calendarTitle, LocalDateTime startDate, LocalDateTime endDate,
                          LocalDateTime time, LocalDateTime alertTime, String note) {
        this.calendarTitle = calendarTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
        this.alertTime = alertTime;
        this.note = note;
    }
}