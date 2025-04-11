package com.frend.planit.domain.chatbot.chatMessage.entity;

import com.frend.planit.domain.chatbot.chatRoom.entity.AIChatRoomEntity;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ai_chat_message")
public class AIChatMessage extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private AIChatRoomEntity AIChatRoom;

    @Column(columnDefinition = "LONGTEXT")
    private String userMessage;

    @Column(columnDefinition = "LONGTEXT")
    private String botMessage;
}
