package com.frend.planit.domain.calendar.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleResponse {

    @JsonProperty("schedule_id")
    private Long id;

    private String scheduleTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime alertTime;
    private String note;
    private String blockColor;

    public static ScheduleResponse from(ScheduleEntity scheduleEntity) {
        return ScheduleResponse.builder()
                .id(scheduleEntity.getId())
                .scheduleTitle(scheduleEntity.getScheduleTitle())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .alertTime(scheduleEntity.getAlertTime())
                .note(scheduleEntity.getNote())
                .blockColor(scheduleEntity.getBlockColor())
                .build();
    }

    public static List<ScheduleResponse> fromList(List<ScheduleEntity> scheduleEntities) {
        return scheduleEntities.stream()
                .map(ScheduleResponse::from)
                .toList();
    }
}
