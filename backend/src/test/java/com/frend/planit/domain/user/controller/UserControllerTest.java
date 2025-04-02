package com.frend.planit.domain.user.controller;

import static com.frend.planit.domain.user.enums.Gender.MALE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.service.UserService;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("닉네임 중복 확인 성공")
    void checkNicknameSuccess() throws Exception {
        // given
        String nickname = "new_user";

        Mockito.when(userService.isNicknameAvailable(nickname)).thenReturn(true);

        // when & then
        mockMvc.perform(get("/api/v1/user/check-nickname")
                        .param("nickname", nickname))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMyInfoSuccess() throws Exception {
        // given
        Long userId = 1L;
        String token = "Bearer test-token";

        UserMeResponse response = new UserMeResponse(
                1L,
                "닉네임",
                "test@email.com",
                "01012345678",
                MALE,
                LocalDate.of(2025, 4, 2),
                "https://example.com/profile.png",
                "자기소개",
                UserStatus.ACTIVE,
                LocalDateTime.of(2025, 4, 2, 12, 0),
                SocialType.GOOGLE,
                LoginType.SOCIAL,
                true,
                Role.USER
        );

        Mockito.when(jwtTokenProvider.getUserIdFromToken("test-token")).thenReturn(userId);
        Mockito.when(userService.getMyInfo(userId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/user/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("01012345678"))
                .andExpect(jsonPath("$.birthDate").value("2025-04-02"))
                .andExpect(jsonPath("$.gender").value("MALE"));
    }

    @Test
    @DisplayName("최초 로그인 추가 정보 입력 성공")
    void updateFirstInfoSuccess() throws Exception {
        // given
        Long userId = 1L;
        String token = "Bearer test-token";

        UserFirstInfoRequest request = new UserFirstInfoRequest(
                "test@email.com",
                "new_nick",
                "01012345678",
                LocalDate.of(2000, 1, 1),
                MALE
        );

        Mockito.when(jwtTokenProvider.getUserIdFromToken("test-token")).thenReturn(userId);
        Mockito.doNothing().when(userService)
                .updateFirstInfo(eq(userId), any(UserFirstInfoRequest.class));

        // when & then
        mockMvc.perform(patch("/api/v1/user/me/first-info")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}