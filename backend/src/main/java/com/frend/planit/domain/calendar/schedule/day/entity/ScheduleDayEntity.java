package com.frend.planit.domain.calendar.schedule.day.entity;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "scheduleDay")
public class ScheduleDayEntity extends BaseTime {

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "day_order", nullable = false)
    private int dayOrder;

    @OneToMany(mappedBy = "scheduleDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TravelEntity> travelList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    public static ScheduleDayEntity of(LocalDate date, int dayOrder) {
        return ScheduleDayEntity.builder()
                .date(date)
                .dayOrder(dayOrder)
                .build();
    }

    // 연관관계 편의 메서드
    public void addTravel(TravelEntity travel) {
        this.travelList.add(travel);
        travel.setScheduleDay(this);
    }

    // 연관관계 설정용 setter
    public void setSchedule(ScheduleEntity schedule) {
        this.schedule = schedule;
    }
}
