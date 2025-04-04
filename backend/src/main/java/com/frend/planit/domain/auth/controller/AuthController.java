package com.frend.planit.domain.auth.controller;

import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.request.TokenRefreshRequest;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.auth.service.AuthService;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 소셜 로그인 (Google, Kakao, Naver)
     */
    @PostMapping("/social-login")
    public ResponseEntity<SocialLoginResponse> socialLogin(
            @RequestBody @Valid SocialLoginRequest request
    ) {
        SocialLoginResponse response = authService.loginOrRegister(request);
        return ResponseEntity.ok(response);
    }

    // Access Token 재발급 API, 클라이언트로부터 전달받은 Refresh Token이 유효할 경우, 새로운 Access Token을 발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(
            @RequestBody @Valid TokenRefreshRequest request
    ) {
        TokenRefreshResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    //토큰 제거하면서 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new ServiceException(ErrorType.UNAUTHORIZED);
        }

        String accessToken = bearer.substring(7);
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}
