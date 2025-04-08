package com.frend.planit.domain.chatbot.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // MockMvc 시큐리티 필터 비활성화
public class AIChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AIChatRoomService aiChatRoomService;

    private AIChatRoom mockRoom;

    @BeforeEach
    void setUp() {

        mockRoom = AIChatRoom.builder()
                .systemMessage("시스템 메시지")
                .systemStrategyMessage("전략 메시지")
                .build();

    }

    @Test
    @DisplayName("채팅방 생성 - 성공")
    void createRoomSuccess() throws Exception {
        // given
        given(aiChatRoomService.createRoom()).willReturn(mockRoom);

        // when & then
        mockMvc.perform(post("/ai/chat/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemMessage").value("시스템 메시지"))
                .andExpect(jsonPath("$.systemStrategyMessage").value("전략 메시지"));
    }
}
