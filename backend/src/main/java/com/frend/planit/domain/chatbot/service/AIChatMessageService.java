package com.frend.planit.domain.chatbot.service;

import com.frend.planit.domain.chatbot.entity.AIChatMessage;
import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.repository.AIChatMessageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AIChatMessageService {

    private final AIChatMessageRepository messageRepository;

    @Transactional
    public List<Message> buildPromptMessages(AIChatRoom aiChatRoom, String userMessage) {
        int lastSummaryIdx = aiChatRoom.getLastSummaryMessageEndMessageIndex();
        List<AIChatMessage> allMessages = aiChatRoom.getMessages();

        List<Message> result = new ArrayList<>();
        result.add(new SystemMessage(aiChatRoom.getSystemMessage()));

        if (!aiChatRoom.getSummaryMessages().isEmpty()) {
            result.add(new SystemMessage(
                    aiChatRoom.getSummaryMessages().getLast().getBotMessage()
            ));
        }

        List<Message> previousMessages = allMessages
                .subList(Math.max(0, lastSummaryIdx + 1), allMessages.size())
                .stream()
                .flatMap(msg -> Stream.of(
                        new UserMessage(msg.getUserMessage()),
                        new AssistantMessage(msg.getBotMessage())
                )).collect(Collectors.toList());

        result.addAll(previousMessages);
        result.add(new UserMessage(userMessage));

        return result;
    }

    // AI 응답 스트리밍과 저장을 분리
    @Async
    public void saveMessageAsync(AIChatRoom aiChatRoom, String userMessage, String botMessage) {
        AIChatMessage message = new AIChatMessage();
        message.setChatRoom(aiChatRoom);
        message.setUserMessage(userMessage);
        message.setBotMessage(botMessage);

        messageRepository.save(message);
    }

}
