package com.frend.planit.domain.calendar.schedule.travel.entity;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import com.frend.planit.domain.calendar.schedule.travel.dto.request.TravelRequest;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel")
public class TravelEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_day_id", nullable = false)
    private ScheduleDayEntity scheduleDay;

    @Column(name = "kakaomap_id", nullable = false)
    private String kakaomapId;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lng", nullable = false)
    private Double lng;

    @Column(name = "visit_hour", nullable = false)
    private int visitHour;

    @Column(name = "visit_minute", nullable = false)
    private int visitMinute;

    public static TravelEntity of(TravelRequest request, ScheduleDayEntity scheduleDay) {
        return TravelEntity.builder()
                .scheduleDay(scheduleDay)
                .kakaomapId(request.getKakaomapId())
                .location(request.getLocation())
                .category(request.getCategory())
                .lat(request.getLat())
                .lng(request.getLng())
                .visitHour(request.getHour())
                .visitMinute(request.getMinute())
                .build();
    }


}
