package com.frend.planit.domain.chatbot.chatMessage.service;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.chatbot.chatMessage.dto.request.AIChatMessageRequest;
import com.frend.planit.domain.chatbot.chatMessage.dto.response.AIChatMessageResponse;
import com.frend.planit.domain.chatbot.chatMessage.entity.AIChatMessage;
import com.frend.planit.domain.chatbot.chatRoom.entity.AIChatRoomEntity;
import com.frend.planit.domain.chatbot.chatRoom.repository.AIChatRoomRepository;
import com.frend.planit.domain.chatbot.chatRoom.service.AIChatRoomService;
import com.frend.planit.domain.chatbot.chatbotUtils.AIUserContextHelper;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AIChatMessageService {

    private final AIChatRoomService aiChatRoomService;
    private final AIChatRoomRepository aiChatRoomRepository;
    private final OpenAiChatModel chatClient;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;


    @Transactional
    public AIChatMessageResponse createMessages(
            Long userId,
            Long chatRoomId,
            AIChatMessageRequest request) {

        // 채팅방 조회
        AIChatRoomEntity chatRoom = aiChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ServiceException(ErrorType.AI_CHAT_ROOM_NOT_FOUND));

        // 사용자 Schedule 조회
        List<ScheduleEntity> userSchedules = scheduleRepository.findAllByUserId(userId);

        // 여행 일정 기반 시스템 메세지 컨텍스트 생성
        String travelContext = AIUserContextHelper.buildUserTravelContext(userSchedules);

        // 시스템 메세지 생성
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(travelContext));

        chatRoom
                .getAIChatMessages()
                .forEach(chatMessage -> {
                    messages.add(new UserMessage(chatMessage.getUserMessage()));
                    messages.add(new AssistantMessage(chatMessage.getBotMessage()));
                });

        messages.add(new UserMessage(request.getUserMessage()));

        String botMessage = chatClient.call(new Prompt(messages))
                .getResult()
                .getOutput()
                .getText();

        // 메세지 저장
        AIChatMessage savedMessage = chatRoom.addChatMessage(request.getUserMessage(), botMessage);
        aiChatRoomRepository.save(chatRoom);

        return AIChatMessageResponse.from(savedMessage);
    }
}
