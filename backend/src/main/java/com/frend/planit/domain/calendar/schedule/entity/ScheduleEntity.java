package com.frend.planit.domain.calendar.schedule.entity;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
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
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private CalendarEntity calendar;

    @Column(name = "schedule_title", nullable = false)
    private String scheduleTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "note")
    private String note;

    @Column(name = "alert_time")
    private LocalTime alertTime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleDayEntity> scheduleDayList = new ArrayList<>();

    // 객체 생성 메서드: 날짜 범위로 day 생성
    public static ScheduleEntity of(CalendarEntity calendar, ScheduleRequest scheduleRequest) {
        ScheduleEntity schedule = ScheduleEntity.builder()
                .calendar(calendar)
                .scheduleTitle(scheduleRequest.getScheduleTitle())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .note(scheduleRequest.getNote())
                .alertTime(scheduleRequest.getAlertTime() != null ? scheduleRequest.getAlertTime()
                        : null)
                .build();

        // ScheduleDay 자동 생성
        LocalDate current = schedule.getStartDate();
        int order = 1;
        while (!current.isAfter(schedule.getEndDate())) {
            ScheduleDayEntity day = ScheduleDayEntity.of(current, order++);
            schedule.addScheduleDay(day);
            current = current.plusDays(1);
        }

        return schedule;
    }

    // 연관관계 편의 메서드
    public void addScheduleDay(ScheduleDayEntity scheduleDay) {
        this.scheduleDayList.add(scheduleDay);
        scheduleDay.setSchedule(this);
    }

    // Test Code에서 사용하기 위한 setter
    public void setId(Long id) {
        this.id = id;
    }

    public void updateSchedule(ScheduleRequest scheduleRequest) {
        this.scheduleTitle = scheduleRequest.getScheduleTitle();
        this.startDate = scheduleRequest.getStartDate();
        this.endDate = scheduleRequest.getEndDate();
        this.note = scheduleRequest.getNote();
        this.alertTime = scheduleRequest.getAlertTime();
    }
}
