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
@Builder(access = AccessLevel.PRIVATE)
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

    @Column(name = "kakaomap_id")
    private String kakaomapId;

    @Column(name = "location")
    private String location;

    @Column(name = "category")
    private String category;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "visit_hour")
    private int visitHour;

    @Column(name = "visit_minute")
    private int visitMinute;

    // 객체 생성 메서드
    public static TravelEntity of(TravelRequest request, ScheduleDayEntity scheduleDay) {
        TravelEntity travel = TravelEntity.builder()
                .scheduleDay(scheduleDay)
                .kakaomapId(request.getKakaomapId())
                .location(request.getLocation())
                .category(request.getCategory())
                .lat(request.getLat())
                .lng(request.getLng())
                .visitHour(request.getHour())
                .visitMinute(request.getMinute())
                .build();

        // 편의 메서드로 연관관계 설정
        scheduleDay.addTravel(travel);

        return travel;
    }

    // 연관관계 설정용 setter
    public void setScheduleDay(ScheduleDayEntity scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public void updateTravel(TravelRequest travelRequest, ScheduleDayEntity scheduleDay) {
        this.scheduleDay = scheduleDay;
        this.location = travelRequest.getLocation();
        this.category = travelRequest.getCategory();
        this.lat = travelRequest.getLat();
        this.lng = travelRequest.getLng();
        this.visitHour = travelRequest.getHour();
        this.visitMinute = travelRequest.getMinute();
    }

    // Test Code에서 사용하기 위한 setter
    public void setId(Long id) {
        this.id = id;
    }
}
