package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TravelResponse {

    private Double lat;
    private Double lng;
    private LocalTime time;

    public static TravelResponse from(TravelEntity travelEntity) {
        return TravelResponse.builder()
                .lat(travelEntity.getLat())
                .lng(travelEntity.getLng())
                .time(travelEntity.getTime())
                .build();
    }
}
