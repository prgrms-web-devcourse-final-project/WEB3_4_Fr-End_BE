package com.frend.planit.domain.calendar.schedule.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank
    @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3])$")
    private String hour;

    @NotBlank
    @Pattern(regexp = "^[0-5][0-9]$")
    private String minute;

    public int getIntHour() {
        return Integer.parseInt(hour);
    }

    public int getIntMinute() {
        return Integer.parseInt(minute);
    }
}
