package com.frend.planit.domain.chatbot.chatRoom.repository;

import com.frend.planit.domain.chatbot.chatRoom.entity.AIChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIChatRoomRepository extends JpaRepository<AIChatRoomEntity, Long> {

}
