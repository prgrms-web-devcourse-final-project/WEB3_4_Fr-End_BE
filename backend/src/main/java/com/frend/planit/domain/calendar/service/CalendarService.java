package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.exception.CalendarNotFoundException;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarNotFoundException("Calendar with id " + id + " not found"));
        return new CalendarResponseDto(calendar);
    }

    public List<CalendarResponseDto> getAllCalendars() {
        return calendarRepository.findAll().stream()
                .map(CalendarResponseDto::new)
                .collect(Collectors.toList());
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