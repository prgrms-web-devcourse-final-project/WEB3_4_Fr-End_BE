package com.frend.planit.domain.chatbot.chatMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIChatMessageRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String userMessage;
}
