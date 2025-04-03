package com.frend.planit.domain.calendar.schedule.travel.TravelUtils;

import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TravelGroupingUtils {

    /**
     * 여행 엔티티 목록을 날짜별로 그룹화하여 DailyTravelResponse 목록으로 변환합니다.
     *
     * @param travels 여행 엔티티 목록
     * @return 날짜별로 그룹화된 DailyTravelResponse 목록
     */
    public static List<DailyTravelResponse> groupTravelsByDate(List<TravelEntity> travels) {
        Map<LocalDate, List<TravelEntity>> travelsByDate = travels.stream()
                .collect(Collectors.groupingBy(travel -> travel.getScheduleDay().getDate()));

        return travelsByDate.entrySet().stream()
                .map(entry -> new DailyTravelResponse(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(TravelResponse::from)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}