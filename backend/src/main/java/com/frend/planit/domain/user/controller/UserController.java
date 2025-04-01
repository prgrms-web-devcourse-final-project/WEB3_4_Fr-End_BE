package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.LoginRequest;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.LoginResponse;
import com.frend.planit.domain.user.service.UserService;
import com.frend.planit.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    // 구글 로그인 (code 받아서 토큰 + 유저 상태 응답)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.loginOrRegister(request.getCode());
        return ResponseEntity.ok(response);
    }


    // 최초 로그인 시 사용자 추가 정보 입력
    @PatchMapping("/me/first-info")
    public ResponseEntity<Void> updateFirstInfo(
            HttpServletRequest request,
            @RequestBody @Valid UserFirstInfoRequest firstInfoRequest
    ) {
        String token = resolveTokenFromHeader(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        userService.updateFirstInfo(userId, firstInfoRequest);
        return ResponseEntity.noContent().build();
    }

    // 닉네임 중복 여부 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean available = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(available);
    }

    // 헤더에서 JWT 토큰 추출
    private String resolveTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new IllegalArgumentException("토큰이 헤더에 없습니다.");
    }
}
