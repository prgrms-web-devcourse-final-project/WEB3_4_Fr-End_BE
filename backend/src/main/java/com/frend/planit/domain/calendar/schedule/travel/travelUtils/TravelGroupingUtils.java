package com.frend.planit.domain.calendar.schedule.travel.travelUtils;

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

        // 날짜 기준으로 그룹핑
        Map<LocalDate, List<TravelEntity>> travelsByDate = travels.stream()
                .collect(Collectors.groupingBy(travel -> travel.getScheduleDay().getDate()));

        // 그룹핑된 날짜별 여행 리스트로 DailyTravelResponse 변환
        return travelsByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<TravelEntity> travelList = entry.getValue();

                    // scheduleDayId는 TravelEntity에서 가져오고, date는 entry.getKey()에서 가져옴
                    Long scheduleDayId = travelList.isEmpty() ? null
                            : travelList.get(0).getScheduleDay().getId();

                    List<TravelResponse> travelResponses = travelList.stream()
                            .map(TravelResponse::from)
                            .collect(Collectors.toList());

                    return new DailyTravelResponse(scheduleDayId, date, travelResponses);
                })

                .collect(Collectors.toList());
    }
}