package com.frend.planit.domain.calendar.schedule.entity;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleDayEntity> scheduleDayList = new ArrayList<>();

    // 객체 생성 메서드: 날짜 범위로 day 생성
    public static ScheduleEntity of(LocalDate startDate, LocalDate endDate) {
        ScheduleEntity schedule = ScheduleEntity.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // 날짜 기반으로 ScheduleDayEntity 자동 생성
        LocalDate current = startDate;
        int dayOrder = 1;
        while (!current.isAfter(endDate)) {
            ScheduleDayEntity day = ScheduleDayEntity.of(current, dayOrder);
            schedule.addScheduleDay(day);
            current = current.plusDays(1);
            dayOrder++;
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
}
