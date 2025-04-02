package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 최초 로그인 시 사용자 추가 정보 입력
     */
    @PatchMapping("/me/first-info")
    public ResponseEntity<Void> updateFirstInfo(
            @RequestBody @Valid UserFirstInfoRequest firstInfoRequest
    ) {
        Long userId = getCurrentUserId();
        userService.updateFirstInfo(userId, firstInfoRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 닉네임 중복 여부 확인
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean available = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(available);
    }

    /**
     * 현재 로그인한 유저 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getMyInfo() {
        Long userId = getCurrentUserId();
        UserMeResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * SecurityContext에서 현재 로그인한 사용자 ID 추출
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
