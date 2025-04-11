package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
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
     * 추가 정보 입력 시 닉네임 중복 여부 확인
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean available = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(available);
    }

    /**
     * 추가 정보 입력 시 이메일 중복 여부 확인
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }

    /**
     * 추가 정보 입력 시 휴대폰 번호 중복 여부 확인
     */
    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhone(@RequestParam String phone) {
        boolean available = userService.isPhoneAvailable(phone);
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

    @GetMapping("/me/activity/mate-post")
    public ResponseEntity<List<MateResponseDto>> getUserActivity(
            @AuthenticationPrincipal Long userId) {
        // 사용자 서비스에서 활동 내역을 조회
        List<MateResponseDto> matePosts = userService.getUserActivity(userId);
        return ResponseEntity.ok(matePosts);
    }

    @GetMapping("/me/activity/mate-comments")
    public ResponseEntity<List<MateCommentResponseDto>> getUserComments(
            @AuthenticationPrincipal Long userId) {
        List<MateCommentResponseDto> comments = userService.getUserCommentsActivity(userId);
        return ResponseEntity.ok(comments);
    }
}
