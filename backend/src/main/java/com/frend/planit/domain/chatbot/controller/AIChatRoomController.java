package com.frend.planit.domain.chatbot.controller;

import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.service.AIChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat/rooms")
@RequiredArgsConstructor
public class AIChatRoomController {

    private final AIChatRoomService aiChatRoomService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "채팅방 생성")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public AIChatRoom createRoom() {

        AIChatRoom aiChatRoom = aiChatRoomService.createRoom();

        return aiChatRoom;
    }
}
