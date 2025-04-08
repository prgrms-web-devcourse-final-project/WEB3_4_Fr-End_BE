package com.frend.planit.domain.chatbot.controller;

import com.frend.planit.domain.chatbot.dto.response.AIChatMessageResponse;
import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/ai/chat/rooms")
@RequiredArgsConstructor
@Tag(name = "AIChatBot Controller", description = "AI 챗봇 컨트롤러")
public class AIChatRoomController {

    private final AIChatRoomService aiChatRoomService;

    @Operation(summary = "채팅방 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AIChatRoom createRoom() {

        AIChatRoom aiChatRoom = aiChatRoomService.createRoom();

        return aiChatRoom;
    }

    @Operation(summary = "채팅방 대화 생성")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{chatRoomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Transactional
    public Flux<ServerSentEvent<String>> generateStream(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "안녕하세요.") String userMessage
    ) {
        return aiChatRoomService.generateStream(chatRoomId, userMessage);
    }

    @Operation(summary = "로그인 사용자의 채팅방 전체 목록 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AIChatRoom> getChatRooms(Principal principal) {

        Long userId = Long.parseLong(principal.getName()); // 로그인한 사용자의 ID

        return aiChatRoomService.findChatRoomsByUserId(userId);
    }

    @Operation(summary = "로그인 사용자의 채팅방 메세지 기록 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{chatRoomId}/messages")
    @Transactional(readOnly = true)
    public List<AIChatMessageResponse> getMessages(
            @PathVariable Long chatRoomId,
            Principal principal
    ) {

        Long userId = Long.parseLong(principal.getName());

        AIChatRoom aiChatRoom = aiChatRoomService.findByIdAndUserId(chatRoomId, userId);

        return aiChatRoom.getMessages()
                .stream()
                .map(AIChatMessageResponse::from)
                .toList();
    }

    @Operation(summary = "로그인 사용자의 채팅방 삭제")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{chatRoomId}")
    @Transactional(readOnly = true)
    public void deleteChatRoom(
            @PathVariable Long chatRoomId,
            Principal principal
    ) {

        Long userId = Long.parseLong(principal.getName());

        aiChatRoomService.deleteChatRoom(chatRoomId, userId);
    }
}
