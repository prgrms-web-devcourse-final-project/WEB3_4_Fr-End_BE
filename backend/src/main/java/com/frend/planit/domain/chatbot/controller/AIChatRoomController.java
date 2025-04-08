package com.frend.planit.domain.chatbot.controller;

import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.transaction.annotation.Transactional;
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
public class AIChatRoomController {

    private final AIChatRoomService aiChatRoomService;

    @Operation(summary = "채팅방 생성")
    @ResponseStatus(HttpStatus.OK)
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
}
