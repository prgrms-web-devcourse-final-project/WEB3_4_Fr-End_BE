package com.frend.planit.domain.calendar.invite.controller;

import com.frend.planit.domain.calendar.invite.service.InviteService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    private static <T> T orThrow(Optional<T> optional, ErrorType errorType) {
        return optional.orElseThrow(() -> errorType.serviceException());
    }

    private final InviteService inviteService;
    private final UserRepository userRepository;

    // 초대 링크 ( 링크 클릭 시, 캘린더 공유)
    @PostMapping("/accept/{inviteCode}")
    public ResponseEntity<Void> acceptInvite(@PathVariable String inviteCode,
                                             @AuthenticationPrincipal Long userId) {
        User user = orThrow(userRepository.findById(userId), USER_NOT_FOUND);
        inviteService.shareCalendarByInvite(inviteCode, user);
        return ResponseEntity.ok().build();
    }

    // 초대 생성
    @PostMapping("/{calendarId}")
    public ResponseEntity<String> createInvitation(@PathVariable Long calendarId) {
        String inviteCode = inviteService.create(calendarId);
        return ResponseEntity.status(201).body(inviteCode); // 201 Created
    }

    // 초대 링크 무효화
    @PostMapping("/invalidate/{inviteCode}")
    public ResponseEntity<Void> invalidateInvitation(@PathVariable String inviteCode) {
        inviteService.invalidate(inviteCode);
        return ResponseEntity.ok().build();
    }
}
