package com.frend.planit.domain.calendar.schedule.service;

import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;
    private final TravelRepository travelRepository;

    // 여행 일정 조회
    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(Long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 생성
    @Transactional
    public ScheduleResponse createSchedule(Long calendarId, ScheduleRequest scheduleRequest) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        if (scheduleRequest.getTravelList() != null && !scheduleRequest.getTravelList().isEmpty()) {
            saveTravelList(savedSchedule.getId(), scheduleRequest.getTravelList());
        }

        return ScheduleResponse.from(savedSchedule);
    }

    // 여행 일정 수정
    @Transactional
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleRequest scheduleRequest) {
        ScheduleEntity scheduleEntity = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 기존 여행 정보 삭제 후 새로운 정보 저장
        if (scheduleRequest.getTravelList() != null) {
            travelRepository.deleteAllByScheduleId(scheduleId);
            saveTravelList(scheduleId, scheduleRequest.getTravelList());
        }

        scheduleEntity.updateSchedule(scheduleRequest);

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 삭제
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        ScheduleEntity scheduleEntity = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        travelRepository.deleteAllByScheduleId(scheduleId);

        scheduleRepository.delete(scheduleEntity);
    }

}
