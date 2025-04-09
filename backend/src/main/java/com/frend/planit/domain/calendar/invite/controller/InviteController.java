package com.frend.planit.domain.calendar.invite.controller;

import com.frend.planit.domain.calendar.invite.service.InviteService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;
    private final UserRepository userRepository;

    //초대 링크 클릭하면 공유
    @PostMapping("/accept/{inviteCode}")
    public ResponseEntity<Void> acceptInvite(@PathVariable String inviteCode,
                                             @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ErrorType.USER_NOT_FOUND.serviceException());

        inviteService.shareCalendarByInvite(inviteCode, user);
        return ResponseEntity.ok().build();
    }

    // 초대 생성
    @PostMapping("/{calendarId}")
    public ResponseEntity<String> createInvitation(@PathVariable Long calendarId) {
        String inviteCode = inviteService.create(calendarId);
        return ResponseEntity.status(201).body(inviteCode);
    }

    // 초대 무효화
    @PostMapping("/invalidate/{inviteCode}")
    public ResponseEntity<Void> invalidateInvitation(@PathVariable String inviteCode) {
        inviteService.invalidate(inviteCode);
        return ResponseEntity.ok().build();
    }
}