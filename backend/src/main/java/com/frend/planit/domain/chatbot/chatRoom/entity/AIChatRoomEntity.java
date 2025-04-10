package com.frend.planit.domain.chatbot.chatRoom.entity;

import com.frend.planit.domain.chatbot.chatMessage.entity.AIChatMessage;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "ai_chatroom")
public class AIChatRoomEntity extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "AIChatRoom", fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<AIChatMessage> AIChatMessages = new ArrayList<>();


    // Bussiness Logic
    public static AIChatRoomEntity of(User user) {
        return AIChatRoomEntity.builder()
                .user(user)
                .build();
    }

    public AIChatMessage addChatMessage(String userMessage, String botMessage) {
        AIChatMessage chatMessages = AIChatMessage.builder()
                .userMessage(userMessage)
                .botMessage(botMessage)
                .AIChatRoom(this)
                .build();

        AIChatMessages.add(chatMessages);

        return chatMessages;
    }
}