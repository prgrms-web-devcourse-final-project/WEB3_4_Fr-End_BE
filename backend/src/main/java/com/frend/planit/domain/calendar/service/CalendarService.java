package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.exception.CalendarException;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarPermissionValidator calendarPermissionValidator;

    @Transactional
    public CalendarResponseDto createCalendar(CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = CalendarEntity.fromDto(requestDto, user);
        CalendarEntity saved = calendarRepository.save(calendar);
        return new CalendarResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(ErrorType.CALENDAR_NOT_FOUND));
        return new CalendarResponseDto(calendar);
    }

    @Transactional(readOnly = true)
    public Page<CalendarResponseDto> getCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable).map(CalendarResponseDto::new);
    }

    @Transactional
    public CalendarResponseDto updateCalendar(Long id, CalendarRequestDto requestDto, User user) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(ErrorType.CALENDAR_NOT_FOUND));

        // 공유자 or 소유자만 수정 가능
        if (!calendarPermissionValidator.hasModifyAccess(calendar, user)) {
            throw new CalendarException(ErrorType.FORBIDDEN);
        }

        calendar.updateCalendar(requestDto);
        return new CalendarResponseDto(calendar);
    }

    @Transactional
    public void deleteCalendar(Long id, User user) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(ErrorType.CALENDAR_NOT_FOUND));

        // 오직 소유자만 삭제 가능
        if (!calendarPermissionValidator.isOwner(calendar, user)) {
            throw new CalendarException(ErrorType.FORBIDDEN);
        }

        calendarRepository.delete(calendar);
    }
}