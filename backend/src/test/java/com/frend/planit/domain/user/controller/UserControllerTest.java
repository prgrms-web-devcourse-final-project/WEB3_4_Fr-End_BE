package com.frend.planit.domain.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.service.UserService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Test
    @DisplayName("추가 정보 입력 성공")
    void updateFirstInfo() throws Exception {
        UserFirstInfoRequest request = new UserFirstInfoRequest(
                "test@example.com",
                "nickname",
                "01012345678",
                LocalDate.of(2000, 1, 1),
                Gender.MALE
        );

        mockMvc.perform(patch("/api/v1/user/me/first-info")
                        .header("Authorization", "Bearer valid-token") // @AuthenticationPrincipal 사용 시 필요
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("닉네임 중복 확인 성공")
    void checkNicknameAvailable() throws Exception {
        when(userService.isNicknameAvailable("nickname")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-nickname")
                        .param("nickname", "nickname"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("이메일 중복 확인 성공")
    void checkEmailAvailable() throws Exception {
        when(userService.isEmailAvailable("test@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-email")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("휴대폰 번호 중복 확인 성공")
    void checkPhoneAvailable() throws Exception {
        when(userService.isPhoneAvailable("01012345678")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-phone")
                        .param("phone", "01012345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMyInfo() throws Exception {
        // 인증된 사용자 정보 세팅
        Long mockUserId = 1L;
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserId, null, null)
        );

        // 응답 DTO 구성
        UserMeResponse response = UserMeResponse.builder()
                .id(mockUserId)
                .nickname("테스트닉네임")
                .email("test@email.com")
                .phone("01012345678")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 1, 1))
                .profileImage("https://img.com/profile.jpg")
                .bio("소개글입니다")
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .socialType(SocialType.GOOGLE)
                .mailingType(true)
                .role(Role.USER)
                .build();

        // userService가 응답하도록 Mock 세팅
        Mockito.when(userService.getMyInfo(mockUserId)).thenReturn(response);

        // 요청 & 응답 검증
        mockMvc.perform(get("/api/v1/user/me")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("테스트닉네임"));
    }

}
