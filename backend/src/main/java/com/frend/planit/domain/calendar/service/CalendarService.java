package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.frend.planit.global.response.ErrorType.CALENDAR_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.NOT_AUTHORIZED;

@Service
@RequiredArgsConstructor
public class CalendarService {

    // Null이면 지정된 예외 표시
    private static <T> T orThrow(Optional<T> optional, ErrorType errorType) {
        return optional.orElseThrow(() -> errorType.serviceException());
    }

    private final CalendarRepository calendarRepository;
    private final CalendarPermissionValidator calendarPermissionValidator;

    // 캘린더 생성, 201응답
    @Transactional
    public CalendarResponseDto createCalendar(CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = CalendarEntity.fromDto(requestDto, user);
        CalendarEntity saved = calendarRepository.save(calendar);
        return new CalendarResponseDto(saved);
    }

    // 특정 캘린더 조회
    @Transactional(readOnly = true)
    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        return new CalendarResponseDto(calendar);
    }

    // 본인 캘린더 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<CalendarResponseDto> getCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(CalendarResponseDto::new);
    }

    // 캘린더 수정 (생성자, 공유자 가능)
    @Transactional
    public CalendarResponseDto updateCalendar(Long id, CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        if (!calendarPermissionValidator.hasModifyAccess(calendar, user)) {
            throw NOT_AUTHORIZED.serviceException();
        }
        calendar.updateCalendar(requestDto);
        return new CalendarResponseDto(calendar);
    }

    // 캘린더 삭제 (생성자만 가능)
    @Transactional
    public void deleteCalendar(Long id, User user) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(id), CALENDAR_NOT_FOUND);
        if (!calendarPermissionValidator.isOwner(calendar, user)) {
            throw NOT_AUTHORIZED.serviceException();
        }
        calendarRepository.delete(calendar);
    }
}