package com.frend.planit.domain.calendar.schedule.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.schedule.dto.request.ScheduleRequest;
import com.frend.planit.domain.calendar.schedule.dto.response.ScheduleResponse;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    // 전체 여행 일정 조회(readOnly = true)
    public List<ScheduleResponse> getAllSchedules(Long calendarId, Long userId) {
        // 여행 일정 존재 여부 확인
        List<ScheduleEntity> scheduleEntities = findAllByCalendarId(calendarId, userId);

        return ScheduleResponse.fronList(scheduleEntities);

    }

    // 단일 여행 일정 조회
    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(Long calendarId, Long scheduleId, Long userId) {
        // 여행 일정 존재 여부 확인
        ScheduleEntity scheduleEntity = findScheduleById(calendarId, scheduleId, userId);

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 생성
    @Transactional
    public ScheduleResponse createSchedule(
            Long calendarId,
            ScheduleRequest scheduleRequest,
            Long userId
    ) {
        // calendar 존재 여부 확인 및 조회
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.CALENDAR_NOT_FOUND));

        // scheduleEntity 생성
        ScheduleEntity scheduleEntity = ScheduleEntity.of(calendar, scheduleRequest);

        // scheduleEntity 저장
        ScheduleEntity createdScheduleEntity = scheduleRepository.save(scheduleEntity);

        return ScheduleResponse.from(createdScheduleEntity);
    }

    // 여행 일정 수정
    @Transactional
    public ScheduleResponse modifySchedule(
            Long calendarId,
            Long scheduleId,
            ScheduleRequest scheduleRequest,
            Long userId
    ) {
        // 여행 일정 존재 여부 확인
        ScheduleEntity scheduleEntity = findScheduleById(calendarId, scheduleId, userId);

        // scheduleEntity 수정
        scheduleEntity.updateSchedule(scheduleRequest);

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 삭제
    @Transactional
    public ScheduleResponse deleteSchedule(Long calendarId, Long scheduleId, Long userId) {
        // 여행 일정 존재 여부 확인
        ScheduleEntity scheduleEntity = findScheduleById(calendarId, scheduleId, userId);

        // scheduleEntity 삭제
        scheduleRepository.delete(scheduleEntity);

        return ScheduleResponse.from(scheduleEntity);
    }

    // 여행 일정 존재 여부 확인
    public ScheduleEntity findScheduleById(Long scheduleId, Long calendarId, Long userId) {

        // 스케줄 존재 여부 확인
        ScheduleEntity schedules = scheduleRepository.findByIdAndCalendarId(scheduleId, calendarId)
                .orElseThrow(() -> new ServiceException(ErrorType.SCHEDULE_NOT_FOUND));

        // 인증된 사용자 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.USER_NOT_FOUND));

        return schedules;
    }

    // 전체 여행 일정 조회 및 스케줄 존재 여부 확인
    public List<ScheduleEntity> findAllByCalendarId(Long calendarId, Long userId) {
        // 여행 일정 존재 여부 확인
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByCalendarId(calendarId);

        // 인증된 사용자 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.USER_NOT_FOUND));

        return scheduleEntities;
    }
}
