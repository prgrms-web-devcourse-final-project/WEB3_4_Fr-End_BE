package com.frend.planit.domain.chatbot.chatRoom.controller;

import com.frend.planit.domain.chatbot.chatRoom.dto.response.AIChatRoomResponse;
import com.frend.planit.domain.chatbot.chatRoom.service.AIChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatBot/chat/rooms")
@Tag(name = "AIChatRoomController", description = "AI 챗봇 채팅방 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class AIChatRoomController {

    private final AIChatRoomService aiChatRoomService;

    @PostMapping
    @Operation(summary = "AI 챗봇 채팅방 생성")
    @ResponseStatus(HttpStatus.CREATED)
    public AIChatRoomResponse createChatRoom(@AuthenticationPrincipal Long userId) {
        return aiChatRoomService.createChatRoom(userId);
    }

}
