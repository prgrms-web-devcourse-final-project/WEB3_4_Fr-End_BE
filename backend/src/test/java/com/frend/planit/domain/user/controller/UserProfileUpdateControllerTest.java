package com.frend.planit.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.service.UserProfileUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserProfileUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileUpdateService userProfileUpdateService;

    private final Long mockUserId = 1L;

    @BeforeEach
    void setupAuthentication() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockUserId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("닉네임 수정 성공")
    void updateNicknameSuccess() throws Exception {
        UserUpdateNicknameRequest request = new UserUpdateNicknameRequest("new_nickname");

        mockMvc.perform(patch("/api/v1/user/me/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("자기소개 수정 성공")
    void updateBioSuccess() throws Exception {
        UserUpdateBioRequest request = new UserUpdateBioRequest("나는 열정적인 개발자입니다!");

        mockMvc.perform(patch("/api/v1/user/me/bio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("프로필 이미지 수정 성공")
    void updateProfileImageSuccess() throws Exception {
        UserUpdateProfileImageRequest request = new UserUpdateProfileImageRequest(
                "https://image.com/pic.jpg");

        mockMvc.perform(patch("/api/v1/user/me/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("메일링 타입 수정 성공")
    void updateMailingTypeSuccess() throws Exception {
        UserUpdateMailingTypeRequest request = new UserUpdateMailingTypeRequest(true);

        mockMvc.perform(patch("/api/v1/user/me/mailing-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("휴대폰 번호 수정 성공")
    void updatePhoneSuccess() throws Exception {
        UserUpdatePhoneRequest request = new UserUpdatePhoneRequest("01012345678");

        mockMvc.perform(patch("/api/v1/user/me/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("이메일 수정 성공")
    void updateEmailSuccess() throws Exception {
        UserUpdateEmailRequest request = new UserUpdateEmailRequest("test@email.com");

        mockMvc.perform(patch("/api/v1/user/me/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
