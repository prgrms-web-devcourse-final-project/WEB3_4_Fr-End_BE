package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.request.UserProfileUpdateRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserFirstInfoRequest firstInfoRequest
    ) {
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
    public ResponseEntity<UserMeResponse> getMyInfo(@AuthenticationPrincipal Long userId) {
        UserMeResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 마이페이지 프로필 수정
     */
    @PatchMapping("/me/profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserProfileUpdateRequest request
    ) {
        userService.updateProfile(userId, request);
        return ResponseEntity.noContent().build();
    }
}
