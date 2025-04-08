package com.frend.planit.domain.calendar.controller;

import com.frend.planit.domain.calendar.dto.request.CalendarRequestDto;
import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.service.CalendarService;
import com.frend.planit.domain.calendar.service.SharedCalendarService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final SharedCalendarService sharedCalendarService;
    private final UserRepository userRepository;

    public CalendarController(CalendarService calendarService,
                              SharedCalendarService sharedCalendarService,
                              UserRepository userRepository) {
        this.calendarService = calendarService;
        this.sharedCalendarService = sharedCalendarService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<CalendarResponseDto> createCalendar(@Valid @RequestBody CalendarRequestDto requestDto) {
        CalendarResponseDto responseDto = calendarService.createCalendar(requestDto);
        URI location = URI.create("/api/calendar/" + responseDto.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> getCalendar(@PathVariable Long id) {
        return ResponseEntity.ok(calendarService.getCalendar(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CalendarResponseDto>> getCalendars(Pageable pageable) {
        Page<CalendarResponseDto> page = calendarService.getCalendars(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> updateCalendar(@PathVariable Long id,
                                                              @Valid @RequestBody CalendarRequestDto requestDto) {
        return ResponseEntity.ok(calendarService.updateCalendar(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }

    // 공유받은 캘린더 목록 조회
    @GetMapping("/shared")
    public ResponseEntity<List<CalendarResponseDto>> getSharedCalendars(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ErrorType.USER_NOT_FOUND.serviceException());

        List<CalendarResponseDto> result = sharedCalendarService.getSharedCalendars(user).stream()
                .map(CalendarResponseDto::new)
                .toList();

        return ResponseEntity.ok(result);
    }
}