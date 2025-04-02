package com.frend.planit.domain.calendar.schedule.travel.service;

import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.calendar.schedule.travel.dto.response.TravelResponse;
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

    // 전체 행선지 조회
    @Transactional(readOnly = true)
    public List<TravelResponse> getAllTravels(Long scheduleId) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 행선지 조회
        List<TravelEntity> travels = travelRepository.findByScheduleId(scheduleId);

        // TravelEntity를 TravelResponse로 변환
        return travels.stream()
                .map(TravelResponse::from)
                .toList();
    }
}
