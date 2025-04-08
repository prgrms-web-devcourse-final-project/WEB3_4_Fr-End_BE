package com.frend.planit.domain.chatbot.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.repository.AIChatRoomRepository;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // MockMvc 시큐리티 필터 비활성화
public class AIChatRoomControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AIChatRoomRepository chatRoomRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AIChatRoomService aiChatRoomService;

    private Long chatRoomId;

    private AIChatRoom chatRoom;

    @BeforeEach
    void setUp() {

        // 테스트용 채팅방 미리 저장
        chatRoom = AIChatRoom.builder()
                .systemMessage("시스템 메시지입니다.")
                .systemStrategyMessage("요약 전략 메시지입니다.")
                .build();

        chatRoomId = chatRoomRepository.save(chatRoom).getId();

    }

    @Test
    @DisplayName("채팅방 생성 - 성공")
    void createRoomSuccess() throws Exception {
        // given
        given(aiChatRoomService.createRoom()).willReturn(chatRoom);

        // when & then
        mockMvc.perform(post("/api/v1/ai/chat/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemMessage").value("시스템 메시지입니다."))
                .andExpect(jsonPath("$.systemStrategyMessage").value("요약 전략 메시지입니다."));
    }

    @Test
    @DisplayName("채팅방 대화 생성 - 성공")
    void generateStream() throws Exception {
        // given
        String userMessage = "안녕하세요.";

        // when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/v1/ai/chat/rooms/{chatRoomId}")
                                .queryParam("userMessage", userMessage)
                                .build(chatRoomId))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange();

        // then
        response.expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextCount(1) // 최소한 1개의 응답이 있는지 확인
                .thenCancel()
                .verify();
    }
}