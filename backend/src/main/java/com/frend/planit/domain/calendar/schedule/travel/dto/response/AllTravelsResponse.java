package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.time.LocalDate;
import java.util.List;

public record AllTravelsResponse(
        String scheduleTitle,
        LocalDate startDate,
        LocalDate endDate,
        List<Long> scheduleDayIds,
        List<DailyTravelResponse> dailyTravels
) {

    public static AllTravelsResponse of(ScheduleEntity schedule, List<Long> scheduleDayIds,
            List<DailyTravelResponse> dailyTravels) {
        return new AllTravelsResponse(
                schedule.getScheduleTitle(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                scheduleDayIds,
                dailyTravels
        );
    }
}