package com.frend.planit.domain.user.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.TestConfig;
import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePasswordRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.domain.user.service.UserProfileUpdateService;
import com.frend.planit.global.security.JwtAuthenticationFilter;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserProfileUpdateController.class)
@ContextConfiguration(classes = {UserProfileUpdateController.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserProfileUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileUpdateService userProfileUpdateService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    private final Long userId = 1L;

    @Test
    @DisplayName("닉네임 수정 API")
    @WithMockUser
    void updateNickname() throws Exception {
        UserUpdateNicknameRequest request = new UserUpdateNicknameRequest("새닉네임");
        doNothing().when(userProfileUpdateService).updateNickname(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/nickname")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("자기소개 수정 API")
    @WithMockUser
    void updateBio() throws Exception {
        UserUpdateBioRequest request = new UserUpdateBioRequest("새로운 소개입니다.");
        doNothing().when(userProfileUpdateService).updateBio(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/bio")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("프로필 이미지 수정 API")
    @WithMockUser
    void updateProfileImage() throws Exception {
        UserUpdateProfileImageRequest request = new UserUpdateProfileImageRequest(
                1, "https://new.image/url.png");
        doNothing().when(userProfileUpdateService).updateProfileImage(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/profile-image")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("메일링 타입 수정 API")
    @WithMockUser
    void updateMailingType() throws Exception {
        UserUpdateMailingTypeRequest request = new UserUpdateMailingTypeRequest(false);
        doNothing().when(userProfileUpdateService).updateMailingType(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/mailing-type")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("휴대폰 번호 수정 API")
    @WithMockUser
    void updatePhone() throws Exception {
        UserUpdatePhoneRequest request = new UserUpdatePhoneRequest("01012345678");
        doNothing().when(userProfileUpdateService).updatePhone(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/phone")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("이메일 수정 API")
    @WithMockUser
    void updateEmail() throws Exception {
        UserUpdateEmailRequest request = new UserUpdateEmailRequest("new@email.com");
        doNothing().when(userProfileUpdateService).updateEmail(userId, request);

        mockMvc.perform(patch("/api/v1/user/me/email")
                        .principal(() -> userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("비밀번호 변경 API - 성공")
    @WithMockUser
    void updatePassword_success() throws Exception {
        Long mockUserId = 1L;

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        ReflectionTestUtils.setField(request, "currentPassword", "1234");
        ReflectionTestUtils.setField(request, "newPassword", "5678");

        User mockUser = User.builder()
                .loginId("testuser")
                .password("encoded1234")
                .loginType(LoginType.LOCAL)
                .build();

        // 👇 id 필드는 직접 설정
        ReflectionTestUtils.setField(mockUser, "id", mockUserId);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);
        when(passwordEncoder.encode("5678")).thenReturn("encoded5678");

        mockMvc.perform(patch("/api/v1/user/me/password")
                        .principal(() -> mockUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

}