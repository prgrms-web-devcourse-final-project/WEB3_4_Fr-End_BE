package com.frend.planit.domain.chatbot.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.frend.planit.domain.chatbot.entity.AIChatMessage;
import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.repository.AIChatRoomRepository;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import java.util.List;
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
import org.springframework.test.util.ReflectionTestUtils;
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
                .andExpect(status().isCreated())
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

    @Test
    @DisplayName("로그인 사용자의 채팅방 전체 목록 조회 - 성공")
    void getChatRoomsSuccess() throws Exception {
        // given
        AIChatRoom room1 = AIChatRoom.builder()
                .systemMessage("시스템 메시지 1")
                .systemStrategyMessage("요약 전략 메시지 1")
                .build();

        AIChatRoom room2 = AIChatRoom.builder()
                .systemMessage("시스템 메시지 2")
                .systemStrategyMessage("요약 전략 메시지 2")
                .build();

        given(aiChatRoomService.findChatRoomsByUserId(1L))
                .willReturn(List.of(room1, room2));

        // when & then
        mockMvc.perform(get("/api/v1/ai/chat/rooms")
                        .with(request -> {
                            request.setUserPrincipal(() -> "1");
                            return request;
                        })) // Principal.getName() → "1"
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // room1
                .andExpect(jsonPath("$[0].systemMessage").value("시스템 메시지 1"))
                .andExpect(jsonPath("$[0].systemStrategyMessage").value("요약 전략 메시지 1"))

                // room2
                .andExpect(jsonPath("$[1].systemMessage").value("시스템 메시지 2"))
                .andExpect(jsonPath("$[1].systemStrategyMessage").value("요약 전략 메시지 2"));
    }

    @Test
    @DisplayName("로그인 사용자의 채팅방 메시지 기록 조회 - 성공")
    void getChatRoomMessagesSuccess() throws Exception {
        // given
        Long chatRoomId = 1L;
        Long messageId1 = 100L;
        Long messageId2 = 101L;

        AIChatMessage message1 = AIChatMessage.builder()
                .userMessage("유저 메시지 1")
                .botMessage("봇 메시지 1")
                .build();
        AIChatMessage message2 = AIChatMessage.builder()
                .userMessage("유저 메시지 2")
                .botMessage("봇 메시지 2")
                .build();

        // messageId 수동 주입
        ReflectionTestUtils.setField(message1, "id", messageId1);
        ReflectionTestUtils.setField(message2, "id", messageId2);

        AIChatRoom room = AIChatRoom.builder()
                .systemMessage("시스템")
                .systemStrategyMessage("전략")
                .messages(List.of(message1, message2))
                .build();

        // chatRoomId 수동 주입
        ReflectionTestUtils.setField(room, "id", chatRoomId);

        // 관계 연결
        message1.setChatRoom(room);
        message2.setChatRoom(room);

        given(aiChatRoomService.findByIdAndUserId(chatRoomId, 1L)).willReturn(room);

        // when & then
        mockMvc.perform(get("/api/v1/ai/chat/rooms/{chatRoomId}/messages", chatRoomId)
                        .with(request -> {
                            request.setUserPrincipal(() -> "1");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // message1
                .andExpect(jsonPath("$[0].id").value(messageId1))
                .andExpect(jsonPath("$[0].chatRoomId").value(chatRoomId))
                .andExpect(jsonPath("$[0].userMessage").value("유저 메시지 1"))
                .andExpect(jsonPath("$[0].botMessage").value("봇 메시지 1"))

                // message2
                .andExpect(jsonPath("$[1].id").value(messageId2))
                .andExpect(jsonPath("$[1].chatRoomId").value(chatRoomId))
                .andExpect(jsonPath("$[1].userMessage").value("유저 메시지 2"))
                .andExpect(jsonPath("$[1].botMessage").value("봇 메시지 2"));
    }

}