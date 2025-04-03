package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TravelResponse {

    @JsonProperty("place_name")
    private String location;

    @JsonProperty("category_group_name")
    private String category;

    @JsonProperty("x")
    private Double lat;

    @JsonProperty("y")
    private Double lng;

    private int hour;

    private int minute;

    public static TravelResponse from(TravelEntity travelEntity) {
        return TravelResponse.builder()
                .location(travelEntity.getLocation())
                .category(travelEntity.getCategory())
                .lat(travelEntity.getLat())
                .lng(travelEntity.getLng())
                .hour(travelEntity.getVisitHour())
                .minute(travelEntity.getVisitMinute())
                .build();
    }
}
