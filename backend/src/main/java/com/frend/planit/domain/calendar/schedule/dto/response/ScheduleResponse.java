package com.frend.planit.domain.calendar.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public static ScheduleResponse from(ScheduleEntity scheduleEntity) {
        return ScheduleResponse.builder()
                .id(scheduleEntity.getId())
                .scheduleTitle(scheduleEntity.getScheduleTitle())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .alertTime(scheduleEntity.getAlertTime())
                .note(scheduleEntity.getNote())
                .build();
    }
}
