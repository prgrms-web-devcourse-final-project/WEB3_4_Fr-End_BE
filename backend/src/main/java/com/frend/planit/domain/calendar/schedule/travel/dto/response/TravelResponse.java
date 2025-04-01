package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TravelResponse {

    private Double lat;
    private Double lng;
    private LocalDateTime plan;
    private String title;
    private String content;

    public static TravelResponse from(TravelEntity travelEntity) {
        return TravelResponse.builder()
                .lat(travelEntity.getLat())
                .lng(travelEntity.getLng())
                .plan(travelEntity.getPlan())
                .title(travelEntity.getTitle())
                .content(travelEntity.getContent())
                .build();
    }
}
