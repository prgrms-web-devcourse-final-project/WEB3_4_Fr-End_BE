package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.service.UserService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 최초 로그인 시 사용자 추가 정보 입력
     */
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
    public ResponseEntity<UserMeResponse> getMyInfo(HttpServletRequest request) {
        String token = resolveTokenFromHeader(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        UserMeResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    private String resolveTokenFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new ServiceException(ErrorType.UNAUTHORIZED);
    }
}
