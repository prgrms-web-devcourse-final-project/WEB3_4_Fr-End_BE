package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.exception.CalendarException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public CalendarResponseDto createCalendar(CalendarRequestDto requestDto) {
        CalendarEntity calendar = CalendarEntity.fromDto(requestDto);
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calendar);
    }

    @Transactional(readOnly = true)
    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(ErrorType.CALENDAR_NOT_FOUND));
        return new CalendarResponseDto(calendar);
    }

    @Transactional(readOnly = true)
    public Page<CalendarResponseDto> getCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(CalendarResponseDto::new);
    }

    @Transactional
    public CalendarResponseDto updateCalendar(Long id, CalendarRequestDto requestDto) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(ErrorType.CALENDAR_NOT_FOUND));

        calendar.updateCalendar(requestDto);
        return new CalendarResponseDto(calendar);
    }

    @Transactional
    public void deleteCalendar(Long id) {
        if (!calendarRepository.existsById(id)) {
            throw new CalendarException(ErrorType.CALENDAR_NOT_FOUND);
        }
        calendarRepository.deleteById(id);
    }
}