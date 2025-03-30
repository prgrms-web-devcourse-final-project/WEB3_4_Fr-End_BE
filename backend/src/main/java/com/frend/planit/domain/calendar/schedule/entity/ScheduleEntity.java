package com.frend.planit.domain.calendar.schedule.entity;

import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class ScheduleEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    @Column(name = "schedule_title", nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "alert_time", nullable = true)
    private LocalTime alertTime;

    @Column(name = "note", nullable = true, length = 1000)
    private String note;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Travel> travelList = new ArrayList<>();

    // Bussiness Logic
    public void createSchedule(Calendar calendar, ScheduleRequest scheduleRequest) {
        return ScheduleEntity.builder()
                .calendar(calendarId)
                .title(scheduleRequest.getTitle())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .alertTime(scheduleRequest.getAlertTime())
                .note(scheduleRequest.getNote())
                .build();
    }

    public void updateSchedule(ScheduleRequest scheduleRequest) {
        ScheduleEntity schedule = ScheduleEntity.builder()
                .title(scheduleRequest.getTitle())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .alertTime(scheduleRequest.getAlertTime())
                .note(scheduleRequest.getNote())
                .travelList(travelResponses)
                .build();
    }
}
