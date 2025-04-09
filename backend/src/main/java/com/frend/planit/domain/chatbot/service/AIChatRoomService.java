package com.frend.planit.domain.chatbot.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import com.frend.planit.domain.calendar.schedule.repository.ScheduleRepository;
import com.frend.planit.domain.chatbot.chatbotUtils.AIUserContextHelper;
import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.repository.AIChatRoomRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AIChatRoomService {

    private final OpenAiChatModel chatClient;
    private final AIChatRoomRepository aiChatRoomRepository;
    private final AIChatMessageService aiChatMessageService;
    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public AIChatRoom createRoom() {
        AIChatRoom aiChatRoom = AIChatRoom
                .builder()
                .systemMessage("""
                        당신은 한국인과 대화하고 있습니다.
                        한국의 문화와 정서를 이해하고 있어야 합니다.
                        최대한 한국어만 사용해야합니다.
                        """
                        .stripIndent()
                )
                .systemStrategyMessage("""
                        당신은 한국인과 대화하고 있습니다.
                        한국의 문화와 정서를 이해하고 있어야 합니다.
                        최대한 한국어만 사용해야합니다.
                        
                        아래 내용의 핵심을 요약해줘
                        """
                        .stripIndent()
                )
                .build();
        return aiChatRoomRepository.save(aiChatRoom);
    }

    public AIChatRoom findById(Long id) {
        return aiChatRoomRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.AI_CHAT_ROOM_NOT_FOUND));
    }

    @Transactional
    public Flux<ServerSentEvent<String>> generateStream(Long chatRoomId, String userMessage) {

        // 채팅방 ID로 AIChatRoom 조회
        AIChatRoom aiChatRoom = findById(chatRoomId);

        // 로그인한 사용자의 ID 가져오기
        Long userId = aiChatRoom.getUser().getId();

        // 사용자 캘린더 조회
        CalendarEntity calendar = calendarRepository.findByUserId(userId);
        if (calendar == null) {
            throw new ServiceException(ErrorType.CALENDAR_NOT_FOUND);
        }

        // 유저의 여행 일정 데이터 가져오기
        List<ScheduleEntity> userSchedules = scheduleRepository.findAllByCalendarId(
                calendar.getId());
        if (userSchedules.isEmpty()) {
            throw new ServiceException(ErrorType.SCHEDULE_NOT_FOUND);
        }

        // 여행 일정 기반 컨텍스트 생성
        String travelContext = AIUserContextHelper.buildUserTravelContext(userSchedules);

        // 메시지 생성 (컨텍스트 + 사용자의 질문)
        List<Message> promptMessages = aiChatMessageService.buildPromptMessages(aiChatRoom,
                userMessage);

        Prompt prompt = new Prompt(promptMessages);

        StringBuilder fullResponse = new StringBuilder();

        return chatClient.stream(prompt)
                .map(chunk -> {
                    if (chunk.getResult() == null ||
                            chunk.getResult().getOutput() == null ||
                            chunk.getResult().getOutput().getText() == null) {

                        String botMessage = fullResponse.toString();
                        aiChatMessageService.saveMessageAsync(aiChatRoom, userMessage, botMessage);
                        return ServerSentEvent.<String>builder().data("[DONE]").build();
                    }

                    String text = chunk.getResult().getOutput().getText();
                    fullResponse.append(text);
                    return ServerSentEvent.<String>builder().data("\"" + text + "\"").build();
                });
    }

    @Transactional
    public List<AIChatRoom> findChatRoomsByUserId(Long userId) {

        List<AIChatRoom> chatRooms = aiChatRoomRepository.findAllByUserId(userId);

        if (chatRooms.isEmpty()) {
            throw new ServiceException(ErrorType.AI_CHAT_ROOM_NOT_FOUND);
        }

        return chatRooms;
    }

    @Transactional
    public AIChatRoom findByIdAndUserId(Long chatRoomId, Long userId) {
        return aiChatRoomRepository.findByIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new ServiceException(ErrorType.AI_CHAT_ROOM_NOT_FOUND));
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId, Long userId) {
        AIChatRoom chatRoom = aiChatRoomRepository.findByIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new ServiceException(ErrorType.AI_CHAT_ROOM_NOT_FOUND));

        aiChatRoomRepository.delete(chatRoom);
    }
}
