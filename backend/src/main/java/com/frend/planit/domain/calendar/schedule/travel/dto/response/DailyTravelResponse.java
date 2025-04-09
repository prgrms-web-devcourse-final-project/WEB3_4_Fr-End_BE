package com.frend.planit.domain.calendar.schedule.travel.dto.response;

import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.time.LocalDate;
import java.util.Comparator;
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
            List<TravelEntity> travelEntities) {

        // 여행 일정 별 시간/분에 따른 정렬
        List<TravelResponse> sortedTravels = travelEntities.stream()
                .sorted(Comparator.comparingInt(TravelEntity::getVisitHour)
                        .thenComparingInt(TravelEntity::getVisitMinute))
                .map(TravelResponse::from)
                .toList();

        return new DailyTravelResponse(scheduleDayId, date, sortedTravels);
    }
}
