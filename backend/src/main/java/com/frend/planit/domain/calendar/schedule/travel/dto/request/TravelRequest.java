package com.frend.planit.domain.calendar.schedule.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long scheduleDayId;

    @NotNull
    @JsonProperty("id")
    private String kakaomapId;

    @NotBlank
    @JsonProperty("place_name")
    private String location;

    @NotBlank
    @JsonProperty("category_group_name")
    private String category;

    @NotNull
    @JsonProperty("x")
    private Double lat;

    @NotNull
    @JsonProperty("y")
    private Double lng;

    @NotNull
    @Min(0)
    @Max(23)
    private int hour;

    @NotNull
    @Min(0)
    @Max(59)
    private int minute;
}
