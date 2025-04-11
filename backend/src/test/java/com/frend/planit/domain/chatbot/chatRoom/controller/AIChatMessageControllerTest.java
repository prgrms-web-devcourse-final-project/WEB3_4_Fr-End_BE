package com.frend.planit.domain.chatbot.chatRoom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.chatbot.chatMessage.dto.request.AIChatMessageRequest;
import com.frend.planit.domain.chatbot.chatMessage.dto.response.AIChatMessageResponse;
import com.frend.planit.domain.chatbot.chatMessage.service.AIChatMessageService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.security.SecurityConfig;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Import(SecurityConfig.class) // 보안 설정 임포트
public class AIChatMessageControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AIChatMessageService aiChatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .apply(springSecurity())
                .build();

        // 인증 유저 mock 설정
        // Mock 유저 및 SecurityContext 설정
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser.getId(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("메시지 작성 성공")
    void createMessage_success() throws Exception {
        // given
        long chatRoomId = 10L;
        AIChatMessageRequest request = new AIChatMessageRequest("안녕 챗봇!");
        AIChatMessageResponse response = new AIChatMessageResponse("안녕 챗봇!", "안녕하세요, 무엇을 도와드릴까요?");

        when(aiChatMessageService.createMessages(eq(1L), eq(chatRoomId), any()))
                .thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                        post("/api/v1/chatBot/chat/rooms/{chatRoomId}/messages", chatRoomId)
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userMessage").value("안녕 챗봇!"))
                .andExpect(jsonPath("$.botMessage").value("안녕하세요, 무엇을 도와드릴까요?"));
    }
}
