package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.exception.CalendarNotFoundException;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new CalendarNotFoundException("Calendar with id " + id + " not found"));
        return new CalendarResponseDto(calendar);
    }

    @Transactional(readOnly = true)
    public Page<CalendarResponseDto> getCalendars(Pageable pageable) {
        return calendarRepository.findAll(pageable)
                .map(CalendarResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<CalendarResponseDto> getAllCalendars() {
        throw new UnsupportedOperationException("Use getCalendars(Pageable) instead of getAllCalendars()");
    }

    @Transactional
    public CalendarResponseDto updateCalendar(Long id, CalendarRequestDto requestDto) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarNotFoundException("Calendar with id " + id + " not found"));

        calendar.updateCalendar(requestDto);
        return new CalendarResponseDto(calendar);
    }

    @Transactional
    public void deleteCalendar(Long id) {
        if (!calendarRepository.existsById(id)) {
            throw new CalendarNotFoundException("Calendar with id " + id + " not found");
        }
        calendarRepository.deleteById(id);
    }
}