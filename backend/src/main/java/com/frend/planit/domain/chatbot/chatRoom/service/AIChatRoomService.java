package com.frend.planit.domain.chatbot.chatRoom.service;

import com.frend.planit.domain.chatbot.chatRoom.dto.response.AIChatRoomResponse;
import com.frend.planit.domain.chatbot.chatRoom.entity.AIChatRoomEntity;
import com.frend.planit.domain.chatbot.chatRoom.repository.AIChatRoomRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AIChatRoomService {

    private final AIChatRoomRepository aiChatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public AIChatRoomResponse createChatRoom(Long userId) {
        // 사용자 조회
        User user = findUserById(userId);

        // AI 챗봇 채팅방 생성
        AIChatRoomEntity chatRoom = AIChatRoomEntity.of(user);

        // AI 챗봇 채팅방 저장
        AIChatRoomEntity savedChatRoom = aiChatRoomRepository.save(chatRoom);

        // AI 챗봇 채팅방 응답 DTO 생성
        return AIChatRoomResponse.from(savedChatRoom);
    }

    // 공통 메서드

    // 사용자 조회
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.USER_NOT_FOUND));
    }
}
