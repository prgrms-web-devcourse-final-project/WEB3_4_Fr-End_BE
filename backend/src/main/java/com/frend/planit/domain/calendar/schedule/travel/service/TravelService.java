package com.frend.planit.domain.calendar.schedule.travel.service;

import com.frend.planit.domain.calendar.schedule.day.repository.ScheduleDayRepository;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.TravelUtils.TravelGroupingUtils;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.DailyTravelResponse;
import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import com.frend.planit.domain.calendar.schedule.travel.repository.TravelRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDayRepository scheduleDayRepository;

    // 행선지 조회
    @Transactional(readOnly = true)
    public List<DailyTravelResponse> getAllTravels(Long scheduleId) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 행선지 조회
        List<TravelEntity> travels = travelRepository.findAllByScheduleId(scheduleId);
        // 스케줄에 속한 행선지가 없는 경우 예외 처리
        if (travels.isEmpty()) {
            throw new ServiceException(ErrorType.TRAVEL_NOT_FOUND);
        }

        return TravelGroupingUtils.groupTravelsByDate(travels);
    }
}
