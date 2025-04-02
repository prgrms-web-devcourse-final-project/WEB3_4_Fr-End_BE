package com.frend.planit.domain.user.controller;

import com.frend.planit.domain.user.dto.request.SocialLoginRequest;
import com.frend.planit.domain.user.dto.response.SocialLoginResponse;
import com.frend.planit.domain.user.service.UserService;
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

    private final UserService userService;

    /**
     * 소셜 로그인 (Google, Kakao, Naver)
     */
    @PostMapping("/social-login")
    public ResponseEntity<SocialLoginResponse> socialLogin(
            @RequestBody @Valid SocialLoginRequest request
    ) {
        SocialLoginResponse response = userService.loginOrRegister(request);
        return ResponseEntity.ok(response);
    }
}
