package com.frend.planit.domain.auth.controller;

import com.frend.planit.domain.auth.dto.request.LocalLoginRequest;
import com.frend.planit.domain.auth.dto.request.LocalRegisterRequest;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.request.TokenRefreshRequest;
import com.frend.planit.domain.auth.dto.response.AuthResponse;
import com.frend.planit.domain.auth.dto.response.LocalRegisterResponse;
import com.frend.planit.domain.auth.dto.response.RedirectUrlResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.auth.service.AuthService;
import com.frend.planit.domain.auth.service.LocalRegisterService;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LocalRegisterService localRegisterService;

    /**
     * 소셜 로그인
     */
    @PostMapping("/social-login")
    public ResponseEntity<AuthResponse> socialLogin(
            @RequestBody @Valid SocialLoginRequest request
    ) {
        AuthResponse response = authService.authentiate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로컬 로그인
     */
    @PostMapping("/local-login")
    public ResponseEntity<AuthResponse> localLogin(
            @RequestBody @Valid LocalLoginRequest request
    ) {
        AuthResponse response = authService.localLogin(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로컬 회원가입
     */
    @PostMapping("/local-register")
    public ResponseEntity<LocalRegisterResponse> register(
            @RequestBody @Valid LocalRegisterRequest request
    ) {
        LocalRegisterResponse response = localRegisterService.register(request);
        return ResponseEntity.ok(response);
    }


    /**
     * 액세스 토큰 재발급
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(
            @RequestBody @Valid TokenRefreshRequest request
    ) {
        TokenRefreshResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        String accessToken = bearer.substring(7);
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }

    /**
     * 소셜 로그인 리디렉션 URL 생성
     */
    @GetMapping("/redirect-url")
    public ResponseEntity<RedirectUrlResponse> getRedirectUri(
            @RequestParam("socialType") SocialType socialType
    ) {
        String redirectUrl = authService.generateRedirectUri(socialType);
        return ResponseEntity.ok(new RedirectUrlResponse(redirectUrl));
    }
}