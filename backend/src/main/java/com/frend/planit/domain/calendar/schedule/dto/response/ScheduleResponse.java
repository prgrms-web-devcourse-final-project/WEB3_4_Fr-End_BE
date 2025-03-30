package com.frend.planit.domain.calendar.schedule.dto.response;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
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

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime alertTime;
    private String note;
    private List<TravelResponse> travelList;

    public static ScheduleResponse from(ScheduleEntity scheduleEntity) {

        List<TravelResponse> travelResponses = scheduleEntity.getTravelList().stream()
                .map(TravelResponse::from) // 스트림의 각 요소에 대해 TravelResponse.from() 메서드를 적용
                .toList();

        return ScheduleResponse.builder()
                .title(scheduleEntity.getTitle())
                .startDate(scheduleEntity.getStartDate())
                .endDate(scheduleEntity.getEndDate())
                .alertTime(scheduleEntity.getAlertTime())
                .note(scheduleEntity.getNote())
                .travelList(travelResponses)
                .build();
    }

}
