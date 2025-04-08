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
    public AIChatMessage addMessage(String userMessage, String botMessage) {
        AIChatMessage message = AIChatMessage
                .builder()
                .chatRoom(this)
                .userMessage(userMessage)
                .botMessage(botMessage)
                .build();
        messages.add(message);

        return message;
    }

    // 다음 메세지가 추가된 후 요약을 새로할 필요가 있는지 체크
    public boolean needToMakeSummaryMessageOnNextMessageAdded() {
        if (messages.size() < PREVIEWS_MESSAGES_COUNT) {
            return false;
        }

        // 다다음 메세지가 생성되기 위해서 요약이 필요하다면
        // 다음 메세지가 생성된 직후 요약을 하는게 맞다.
        // 그렇기 때문에 여기서 다다음 메세지 번호를 기준으로 계산한다.
        int nextNextMessageNo = messages.size() + 2;

        // 판별공식 : 다다음_메세지_번호 - N > 마지막_요약_메세지_번호
        return nextNextMessageNo - PREVIEWS_MESSAGES_COUNT > getLastSummaryMessageEndMessageNo();
    }

    public int getLastSummaryMessageEndMessageIndex() {
        if (summaryMessages.isEmpty()) {
            return -1;
        }

        return summaryMessages.getLast().getEndMessageIndex();
    }

    public int getLastSummaryMessageEndMessageNo() {
        return getLastSummaryMessageEndMessageIndex() + 1;
    }

    public String genNewSummarySourceMessage(String userMessage, String botMessage) {
        StringBuilder messageBuilder = new StringBuilder();

        // 가장 마지막 요약 내용이 있을 경우, 그것을 먼저 추가한다
        if (!summaryMessages.isEmpty()) {
            // 마지막 요약 메시지 내용 추가
            messageBuilder.append(summaryMessages.getLast().getBotMessage());
            messageBuilder.append("\n"); // 줄바꿈
            messageBuilder.append("\n"); // 한 줄 더 띄움 (가독성용)
        }

        // 새로운 요약 구간 시작 안내 텍스트 추가 (예: "== 10번 ~ 20번 내용 요약 ==")
        int startMessageIndex = getLastSummaryMessageEndMessageIndex() + 1;
        int endMessageIndex = getMessages().size() - 1;

        messageBuilder.append(
                "== %d번 ~ %d번 내용 요약 ==".formatted(startMessageIndex, endMessageIndex + 1));
        messageBuilder.append("\n");

        // startMessageIndex부터 endMessageIndex 전까지 반복하면서 Q&A 형식으로 메시지 정리
        for (int i = startMessageIndex; i <= endMessageIndex; i++) {
            AIChatMessage message = messages.get(i); // 각 메시지 가져오기
            messageBuilder.append("Q: ").append(message.getUserMessage())
                    .append("\n"); // 사용자 질문 추가
            messageBuilder.append("A: ").append(message.getBotMessage())
                    .append("\n"); // 챗봇 응답 추가
            messageBuilder.append("\n"); // 각 QA 쌍 사이에 한 줄 띄움
        }

        messageBuilder.append("Q: ").append(userMessage).append("\n");
        messageBuilder.append("A: ").append(botMessage).append("\n");
        messageBuilder.append("\n");

        return messageBuilder.toString();
    }

    // 요약 메시지 생성
    public void addSummaryMessage(String forSummaryUserMessage, String forSummaryBotMessage) {
        AIChatSummaryMessage summaryMessage = AIChatSummaryMessage
                .builder()
                .chatRoom(this) // 현재 채팅방과 연결
                .userMessage(forSummaryUserMessage)
                .botMessage(forSummaryBotMessage)
                .startMessageIndex(getLastSummaryMessageEndMessageIndex() + 1)
                .endMessageIndex(getMessages().size() - 1)
                .build();

        summaryMessages.add(summaryMessage);
    }

}
