package com.frend.planit.domain.chatbot.service;

import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import com.frend.planit.domain.chatbot.repository.AIChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIChatRoomService {

    private final AIChatRoomRepository aiChatRoomRepository;

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
}
