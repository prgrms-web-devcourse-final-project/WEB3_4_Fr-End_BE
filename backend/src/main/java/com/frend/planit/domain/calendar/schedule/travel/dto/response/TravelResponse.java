package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TravelResponse {

    private String location;
    private String category;
    private Double lat;
    private Double lng;
    private int hour;
    private int minute;

    public static TravelResponse from(TravelEntity travelEntity) {
        return TravelResponse.builder()
                .location(travelEntity.getLocation())
                .category(travelEntity.getCategory())
                .lat(travelEntity.getLat())
                .lng(travelEntity.getLng())
                .hour(travelEntity.getHour())
                .minute(travelEntity.getMinute())
                .build();
    }
}
