package com.frend.planit.domain.chatbot.chatMessage.dto.response;

import com.frend.planit.domain.chatbot.chatMessage.entity.AIChatMessage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AIChatMessageResponse {

    @NotNull
    private final String userMessage;

    @NotNull
    private final String botMessage;

    public static AIChatMessageResponse from(AIChatMessage chatMessage) {
        return new AIChatMessageResponse(
                chatMessage.getUserMessage(),
                chatMessage.getBotMessage()
        );
    }
}
