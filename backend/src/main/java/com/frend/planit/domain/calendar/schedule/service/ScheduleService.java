package com.frend.planit.domain.calendar.schedule.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;

    // 여행 일정 조회
    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleDetails(Long calendarId, Long scheduleId) {
        // 스케줄 존재 여부 확인
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // scheduleEntity의 calendarId와 요청한 calendarId가 일치하는지 확인
        if (!scheduleEntity.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException(ErrorType.CALENDAR_NOT_FOUND);
        }

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 생성
    @Transactional
    public ScheduleResponse createScheduleDetails(Long calendarId, Long scheduleId,
            ScheduleRequest scheduleRequest) {
        // 스케줄 존재 여부 확인
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 캘린더 존재 여부 확인
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // scheduleEntity 생성
        ScheduleEntity scheduleEntity = ScheduleEntity.of(calendar, scheduleRequest);

        // scheduleEntity의 calendarId와 요청한 calendarId가 일치하는지 확인
        if (!scheduleEntity.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException(ErrorType.CALENDAR_NOT_FOUND);
        }

        ScheduleEntity createdScheduleEntity = scheduleRepository.save(scheduleEntity);

        return ScheduleResponse.from(createdScheduleEntity);
    }

    // 여행 일정 수정
    @Transactional
    public ScheduleResponse modifyScheduleDetails(Long calendarId, Long scheduleId,
            ScheduleRequest scheduleRequest) {
        // 스케줄 존재 여부 확인
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 캘린더 존재 여부 확인
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // scheduleEntity의 calendarId와 요청한 calendarId가 일치하는지 확인
        if (!scheduleEntity.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException(ErrorType.CALENDAR_NOT_FOUND);
        }

        // scheduleEntity 수정
        scheduleEntity.updateSchedule(scheduleRequest);

        return ScheduleResponse.from(scheduleEntity);
    }

    @Transactional
    public ScheduleResponse deleteScheduleDetails(Long calendarId, Long scheduleId,
            ScheduleRequest scheduleRequest) {
        // 스케줄 존재 여부 확인
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 캘린더 존재 여부 확인
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // scheduleEntity의 calendarId와 요청한 calendarId가 일치하는지 확인
        if (!scheduleEntity.getCalendar().getId().equals(calendarId)) {
            throw new ServiceException(ErrorType.CALENDAR_NOT_FOUND);
        }

        // scheduleEntity 삭제
        scheduleRepository.delete(scheduleEntity);

        return ScheduleResponse.from(scheduleEntity);
    }
}
