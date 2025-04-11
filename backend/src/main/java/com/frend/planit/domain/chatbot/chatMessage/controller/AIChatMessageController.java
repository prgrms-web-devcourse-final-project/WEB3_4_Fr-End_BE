package com.frend.planit.domain.chatbot.chatMessage.controller;

import com.frend.planit.domain.chatbot.chatMessage.dto.request.AIChatMessageRequest;
import com.frend.planit.domain.chatbot.chatMessage.dto.response.AIChatMessageResponse;
import com.frend.planit.domain.chatbot.chatMessage.service.AIChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatBot/chat/rooms/{chatRoomId}/messages")
@RequiredArgsConstructor
@Tag(name = "AIChatMessageController", description = "AI 챗봇 채팅 메세지 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class AIChatMessageController {

    private final AIChatMessageService aiChatMessageService;

    @PostMapping
    @Operation(summary = "메세지 작성")
    @Transactional
    public AIChatMessageResponse createMessages(
            @AuthenticationPrincipal Long userId,
            @PathVariable long chatRoomId,
            @RequestBody @Valid AIChatMessageRequest request
    ) {
        return aiChatMessageService.createMessages(userId, chatRoomId, request);
    }
}
