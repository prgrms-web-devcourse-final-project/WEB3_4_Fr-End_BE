package com.frend.planit.domain.chatbot.chatRoom.dto.response;

import com.frend.planit.domain.chatbot.chatRoom.entity.AIChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AIChatRoomResponse {

    private final Long id;
    private final Long userId;

    // 기존 생성자 (Entity 기반)
    public AIChatRoomResponse(AIChatRoomEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
    }

    // 새로운 생성자 (DTO 기반)
    public static AIChatRoomResponse from(AIChatRoomEntity entity) {
        return new AIChatRoomResponse(
                entity.getId(),
                entity.getUser().getId()
        );
    }
}
