package com.frend.planit.domain.calendar.invite.controller;

import com.frend.planit.domain.calendar.dto.response.CalendarResponseDto;
import com.frend.planit.domain.calendar.invite.entity.InviteEntity;
import com.frend.planit.domain.calendar.invite.service.InviteService;
import com.frend.planit.domain.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;
    private final CalendarService calendarService;

    // 1. 초대 링크 생성
    @PostMapping("/{calendarId}")
    public ResponseEntity<String> createInvitation(@PathVariable Long calendarId) {
        String inviteCode = inviteService.create(calendarId);
        return ResponseEntity.status(201).body(inviteCode);
    }

    // 2. 초대 링크 무효화
    @PostMapping("/invalidate/{inviteCode}")
    public ResponseEntity<Void> invalidateInvitation(@PathVariable String inviteCode) {
        inviteService.invalidate(inviteCode);
        return ResponseEntity.ok().build();
    }

    // 3. 초대 코드로 캘린더 접근
    @GetMapping("/{inviteCode}")
    public ResponseEntity<CalendarResponseDto> getCalendarByInviteCode(@PathVariable String inviteCode) {
        InviteEntity invite = inviteService.findValidInvite(inviteCode);
        CalendarResponseDto response = new CalendarResponseDto(invite.getCalendar());
        return ResponseEntity.ok(response);
    }
}