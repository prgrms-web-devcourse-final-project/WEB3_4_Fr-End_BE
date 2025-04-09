package com.frend.planit.domain.chatbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "AICHAT_MESSAGE")
public class AIChatMessage extends BaseTime {

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    @JsonBackReference // 무한 재귀 순환으로 인해 JSON 직렬화 시 제외
    private AIChatRoom chatRoom;

    @Column(columnDefinition = "LONGTEXT")
    private String userMessage;

    @Column(columnDefinition = "LONGTEXT")
    private String botMessage;

}
