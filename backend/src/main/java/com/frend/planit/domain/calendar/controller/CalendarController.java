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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private static <T> T orThrow(java.util.Optional<T> optional, ErrorType errorType) {
        return optional.orElseThrow(() -> errorType.serviceException());
    }

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

    // 캘린더 생성 (유저ID조회)
    @PostMapping
    public ResponseEntity<CalendarResponseDto> createCalendar(@AuthenticationPrincipal Long userId,
                                                              @Valid @RequestBody CalendarRequestDto requestDto) {
        User user = orThrow(userRepository.findById(userId), USER_NOT_FOUND);
        CalendarResponseDto responseDto = calendarService.createCalendar(requestDto, user);
        URI location = URI.create("/api/calendar/" + responseDto.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    // 캘린더 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> getCalendar(@PathVariable Long id) {
        return ResponseEntity.ok(calendarService.getCalendar(id));
    }

    // 개인 캘린더 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<CalendarResponseDto>> getCalendars(Pageable pageable) {
        Page<CalendarResponseDto> page = calendarService.getCalendars(pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    // 캘린더 수정 (생성자, 공유자 가능)
    @PutMapping("/{id}")
    public ResponseEntity<CalendarResponseDto> updateCalendar(@PathVariable Long id,
                                                              @AuthenticationPrincipal Long userId,
                                                              @Valid @RequestBody CalendarRequestDto requestDto) {
        User user = orThrow(userRepository.findById(userId), USER_NOT_FOUND);
        return ResponseEntity.ok(calendarService.updateCalendar(id, requestDto, user));
    }

    // 캘린더 삭제 (생성자만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id,
                                               @AuthenticationPrincipal Long userId) {
        User user = orThrow(userRepository.findById(userId), USER_NOT_FOUND);
        calendarService.deleteCalendar(id, user);
        return ResponseEntity.noContent().build();
    }

    // 공유된 캘린더 목록 조회
    @GetMapping("/shared")
    public ResponseEntity<List<CalendarResponseDto>> getSharedCalendars(@AuthenticationPrincipal Long userId) {
        User user = orThrow(userRepository.findById(userId), USER_NOT_FOUND);
        List<CalendarResponseDto> result = sharedCalendarService.getSharedCalendars(user).stream()
                .map(CalendarResponseDto::new)
                .toList();
        return ResponseEntity.ok(result);
    }
}