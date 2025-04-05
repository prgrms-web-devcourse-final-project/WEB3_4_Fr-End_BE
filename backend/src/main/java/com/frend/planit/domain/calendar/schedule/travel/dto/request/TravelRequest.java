package com.frend.planit.domain.calendar.schedule.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // 테스트 코드를 위해 추가
public class TravelRequest {

    private Long scheduleDayId;

    @JsonProperty("id")
    private String kakaomapId;

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
}
