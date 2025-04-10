package com.frend.planit.domain.user.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.TestConfig;
import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.service.UserProfileUpdateService;
import com.frend.planit.global.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
                "https://new.image/url.png");
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
}
