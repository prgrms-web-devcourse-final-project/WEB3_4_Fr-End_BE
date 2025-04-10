package com.frend.planit.domain.user.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.TestConfig;
import com.frend.planit.domain.auth.controller.AuthController;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.service.UserService;
import com.frend.planit.global.security.JwtAuthenticationFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {UserController.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("최초 로그인 추가 정보 입력 API")
    void updateFirstInfo() throws Exception {
        Long mockUserId = 1L;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUserId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserFirstInfoRequest request = new UserFirstInfoRequest(
                "test@email.com",
                "닉네임",
                "01012345678",
                LocalDate.of(1998, 1, 1),
                Gender.MALE
        );

        doNothing().when(userService).updateFirstInfo(mockUserId, request);

        mockMvc.perform(patch("/api/v1/user/me/first-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("닉네임 중복 확인 API")
    void checkNickname() throws Exception {
        when(userService.isNicknameAvailable("닉네임")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-nickname")
                        .param("nickname", "닉네임"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("이메일 중복 확인 API")
    void checkEmail() throws Exception {
        when(userService.isEmailAvailable("test@email.com")).thenReturn(false);

        mockMvc.perform(get("/api/v1/user/check-email")
                        .param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("휴대폰 번호 중복 확인 API")
    void checkPhone() throws Exception {
        when(userService.isPhoneAvailable("010-1234-5678")).thenReturn(true);

        mockMvc.perform(get("/api/v1/user/check-phone")
                        .param("phone", "010-1234-5678"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("내 정보 조회 API")
    void getMyInfo() throws Exception {
        Long mockUserId = 1L;

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUserId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserMeResponse response = UserMeResponse.builder()
                .id(mockUserId)
                .nickname("닉네임")
                .email("test@email.com")
                .phone("010-1234-5678")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1998, 1, 1))
                .profileImage("https://image.test.com/profile.png")
                .bio("자기소개입니다.")
                .status(UserStatus.REGISTERED)
                .createdAt(LocalDateTime.now())
                .socialType(SocialType.GOOGLE)
                .mailingType(true)
                .role(Role.USER)
                .build();

        when(userService.getMyInfo(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("닉네임"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.birthDate").value("1998-01-01"))
                .andExpect(jsonPath("$.profileImage").value("https://image.test.com/profile.png"))
                .andExpect(jsonPath("$.bio").value("자기소개입니다."))
                .andExpect(jsonPath("$.status").value("REGISTERED"))
                .andExpect(jsonPath("$.socialType").value("GOOGLE"))
                .andExpect(jsonPath("$.mailingType").value(true))
                .andExpect(jsonPath("$.role").value("USER"));
    }


}
