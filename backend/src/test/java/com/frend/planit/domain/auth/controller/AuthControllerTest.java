package com.frend.planit.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.TestConfig;
import com.frend.planit.domain.auth.dto.request.LocalLoginRequest;
import com.frend.planit.domain.auth.dto.request.LocalRegisterRequest;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.request.TokenRefreshRequest;
import com.frend.planit.domain.auth.dto.response.AuthResponse;
import com.frend.planit.domain.auth.dto.response.LocalRegisterResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.auth.service.AuthService;
import com.frend.planit.domain.auth.service.LocalRegisterService;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.global.security.JwtAuthenticationFilter;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {AuthController.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private LocalRegisterService localRegisterService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    @DisplayName("소셜 로그인 API")
    void socialLogin() throws Exception {
        SocialLoginRequest request = new SocialLoginRequest(SocialType.GOOGLE, "code");
        AuthResponse response = new AuthResponse("access", "refresh", false, "email@test.com");

        when(authService.authentiate(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/social-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"));
    }

    @Test
    @DisplayName("로컬 로그인 API")
    void localLogin() throws Exception {
        LocalLoginRequest request = new LocalLoginRequest("loginId", "password");
        AuthResponse response = new AuthResponse("access", "refresh", false, "email@test.com");

        when(authService.localLogin(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/local-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"));
    }

    @Test
    @DisplayName("로컬 회원가입 API")
    void localRegister() throws Exception {
        LocalRegisterRequest request = new LocalRegisterRequest(
                "ricky50",
                "TestPassword123!",
                "Mark",
                "william51@hotmail.com",
                "001-996-505-9738x98764",
                "소개입니다.",
                "https://www.example.com/image.jpg",
                Gender.FEMALE,
                LocalDate.of(2001, 9, 9),
                false
        );
        LocalRegisterResponse response = new LocalRegisterResponse(1L, "ricky50", "Mark",
                "william51@hotmail.com");

        when(localRegisterService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/local-register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @DisplayName("액세스 토큰 재발급 API")
    void refreshAccessToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest("refreshToken");
        TokenRefreshResponse response = new TokenRefreshResponse("newAccess", "newRefresh");

        when(authService.refreshAccessToken(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccess"));
    }

    @Test
    @DisplayName("소셜 로그인 리디렉션 URL API")
    void redirectUrl() throws Exception {
        when(authService.generateRedirectUri(any())).thenReturn("https://redirect.test");

        mockMvc.perform(get("/api/v1/auth/redirect-url")
                        .param("socialType", "GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirectUri").value("https://redirect.test"));
    }

    @Test
    @DisplayName("로그아웃 API")
    void logout() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isNoContent());
    }
}
