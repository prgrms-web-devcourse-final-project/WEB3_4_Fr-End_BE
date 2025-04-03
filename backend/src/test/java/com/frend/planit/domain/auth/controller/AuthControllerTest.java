package com.frend.planit.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.auth.client.GoogleOauthClient;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.request.TokenRefreshRequest;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.auth.service.AuthService;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private GoogleOauthClient googleOauthClient;

    @Test
    @DisplayName("소셜 로그인 성공")
    void socialLoginSuccess() throws Exception {
        SocialLoginRequest request = new SocialLoginRequest(SocialType.GOOGLE, "test-code");
        SocialLoginResponse response = new SocialLoginResponse("access-token", "refresh-token",
                UserStatus.ACTIVE);

        Mockito.when(authService.authentiate(any(SocialLoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/social-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("accessToken + refreshToken 재발급 성공")
    void refreshAccessToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest("valid-refresh-token");
        TokenRefreshResponse response = new TokenRefreshResponse("new-access-token",
                "new-refresh-token");

        Mockito.when(authService.refreshAccessToken("valid-refresh-token"))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token")); // ✅ 추가됨
    }

    @Test
    @DisplayName("accessToken 재발급 실패 - 유효하지 않은 refreshToken")
    void refreshAccessToken_invalidToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest("invalid-refresh-token");

        Mockito.when(authService.refreshAccessToken("invalid-refresh-token"))
                .thenThrow(new ServiceException(ErrorType.REQUEST_NOT_VALID));

        mockMvc.perform(post("/api/v1/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout() throws Exception {
        String token = "Bearer test-access-token";

        Mockito.doNothing().when(authService).logout("test-access-token");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
