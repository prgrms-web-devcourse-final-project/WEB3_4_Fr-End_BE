package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.dto.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.CalendarResponseDto;
import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public CalendarResponseDto createCalendar(CalendarRequestDto requestDto) {
        CalendarEntity calendar = new CalendarEntity(
                requestDto.getCalendarTitle(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.getTime(),
                requestDto.getAlertTime(),
                requestDto.getNote()
        );
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calendar);
    }

    public CalendarResponseDto getCalendar(Long id) {
        CalendarEntity calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found"));
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
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found"));
        calendar.setCalendarTitle(requestDto.getCalendarTitle());
        calendar.setStartDate(requestDto.getStartDate());
        calendar.setEndDate(requestDto.getEndDate());
        calendar.setTime(requestDto.getTime());
        calendar.setAlertTime(requestDto.getAlertTime());
        calendar.setNote(requestDto.getNote());
        return new CalendarResponseDto(calendar);
    }

    public void deleteCalendar(Long id) {
        calendarRepository.deleteById(id);
    }
}