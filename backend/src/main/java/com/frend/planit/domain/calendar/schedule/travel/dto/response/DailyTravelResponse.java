package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class DailyTravelResponse {

    private final LocalDate date;
    private final int travelCount;
    private final List<TravelResponse> travels;

    public DailyTravelResponse(LocalDate date, List<TravelResponse> travels) {
        this.date = date;
        this.travels = travels;
        this.travelCount = travels.size();
    }
}
