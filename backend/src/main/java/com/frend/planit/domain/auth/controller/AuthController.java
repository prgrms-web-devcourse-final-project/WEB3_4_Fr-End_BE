package com.frend.planit.domain.auth.controller;

import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.auth.service.AuthService;
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
}
