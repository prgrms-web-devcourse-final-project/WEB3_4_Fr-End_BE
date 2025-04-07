package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class DailyTravelResponse {

    private final Long scheduleDayId;
    private final LocalDate date;
    private final int travelCount;
    private final List<TravelResponse> travels;

    public DailyTravelResponse(Long scheduleDayId, LocalDate date, List<TravelResponse> travels) {
        this.scheduleDayId = scheduleDayId;
        this.date = date;
        this.travels = travels;
        this.travelCount = travels != null ? travels.size() : 0;
    }

    public static DailyTravelResponse of(Long scheduleDayId, LocalDate date,
            List<TravelResponse> travelList) {
        return new DailyTravelResponse(scheduleDayId, date, travelList);
    }
}
