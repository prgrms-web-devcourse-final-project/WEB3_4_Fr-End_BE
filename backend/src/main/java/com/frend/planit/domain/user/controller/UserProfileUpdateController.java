package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.service.UserProfileUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/me")
public class UserProfileUpdateController {

    private final UserProfileUpdateService userProfileUpdateService;

    // 닉네임 수정
    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateNicknameRequest request
    ) {
        userProfileUpdateService.updateNickname(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 자기소개 수정
    @PatchMapping("/bio")
    public ResponseEntity<Void> updateBio(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateBioRequest request
    ) {
        userProfileUpdateService.updateBio(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 프로필 이미지 수정
    @PatchMapping("/profile-image")
    public ResponseEntity<Void> updateProfileImage(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateProfileImageRequest request
    ) {
        userProfileUpdateService.updateProfileImage(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 메일링 타입 수정
    @PatchMapping("/mailing-type")
    public ResponseEntity<Void> updateMailingType(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateMailingTypeRequest request
    ) {
        userProfileUpdateService.updateMailingType(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 휴대폰 번호 수정
    @PatchMapping("/phone")
    public ResponseEntity<Void> updatePhone(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdatePhoneRequest request
    ) {
        userProfileUpdateService.updatePhone(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 이메일 수정 (→ 추후 인증 기능 연동 예정)
    @PatchMapping("/email")
    public ResponseEntity<Void> updateEmail(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid UserUpdateEmailRequest request
    ) {
        userProfileUpdateService.updateEmail(userId, request);
        return ResponseEntity.noContent().build();
    }
}
