package com.frend.planit.domain.chatbot.entity;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "AICHAT_ROOM")
public class AIChatRoom extends BaseTime {

    public static final int PREVIEWS_MESSAGES_COUNT = 3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String systemMessage;

    private String systemStrategyMessage;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AIChatMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AIChatSummaryMessage> summaryMessages = new ArrayList<>();


    // Business Logic
    public int getLastSummaryMessageEndMessageIndex() {
        if (summaryMessages.isEmpty()) {
            return -1;
        }

        return summaryMessages.getLast().getEndMessageIndex();
    }

}
