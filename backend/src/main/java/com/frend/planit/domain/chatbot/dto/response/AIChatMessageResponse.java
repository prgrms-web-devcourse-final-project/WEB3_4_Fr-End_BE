package com.frend.planit.domain.chatbot.dto.response;

import com.frend.planit.domain.chatbot.entity.AIChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AIChatMessageResponse {

    private long id;
    private long chatRoomId;
    private String userMessage;
    private String botMessage;

    public static AIChatMessageResponse from(AIChatMessage message) {
        return AIChatMessageResponse.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .userMessage(message.getUserMessage())
                .botMessage(message.getBotMessage())
                .build();
    }

}
