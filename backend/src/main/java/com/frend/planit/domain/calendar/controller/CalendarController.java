package com.frend.planit.domain.calendar.controller;

import com.frend.planit.domain.calendar.dto.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.CalendarResponseDto;
import com.frend.planit.domain.calendar.service.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping
    public ResponseEntity<CalendarResponseDto> createCalendar(@RequestBody CalendarRequestDto requestDto) {
        return ResponseEntity.ok(calendarService.createCalendar(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> getCalendar(@PathVariable Long id) {
        return ResponseEntity.ok(calendarService.getCalendar(id));
    }

    @GetMapping
    public ResponseEntity<List<CalendarResponseDto>> getAllCalendars() {
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> updateCalendar(@PathVariable Long id, @RequestBody CalendarRequestDto requestDto) {
        return ResponseEntity.ok(calendarService.updateCalendar(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }
}