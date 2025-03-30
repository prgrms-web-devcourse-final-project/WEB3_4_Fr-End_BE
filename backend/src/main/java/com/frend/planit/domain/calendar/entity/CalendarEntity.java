package com.frend.planit.domain.calendar.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calendar_title", nullable = false)
    private String calendarTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "alert_time")
    private LocalDateTime alertTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "note")
    private String note;



    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }


    public CalendarEntity() {}

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
