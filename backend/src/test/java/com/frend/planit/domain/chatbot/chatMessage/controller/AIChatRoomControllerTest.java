package com.frend.planit.domain.chatbot.chatMessage.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.frend.planit.domain.chatbot.chatRoom.dto.response.AIChatRoomResponse;
import com.frend.planit.domain.chatbot.chatRoom.repository.AIChatRoomRepository;
import com.frend.planit.domain.chatbot.chatRoom.service.AIChatRoomService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.security.JwtTokenProvider;
import com.frend.planit.global.security.SecurityConfig;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
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
public class AIChatRoomControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIChatRoomRepository aiChatRoomRepository;

    @MockitoBean
    private AIChatRoomService aiChatRoomService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .apply(springSecurity())
                .build();

        // Mock 유저 및 SecurityContext 설정
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser.getId(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("채팅방 생성")
    void createChatRoom() throws Exception {
        // given
        AIChatRoomResponse fakeResponse = new AIChatRoomResponse(100L, 1L);

        when(aiChatRoomService.createChatRoom(1L)).thenReturn(fakeResponse);

        // when
        ResultActions result = mockMvc.perform(
                        post("/api/v1/chatBot/chat/rooms")
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.userId").value(1L));
    }
}
