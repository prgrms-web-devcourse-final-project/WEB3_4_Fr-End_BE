package com.frend.planit.domain.chatbot.repository;

import com.frend.planit.domain.chatbot.entity.AIChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIChatRoomRepository extends JpaRepository<AIChatRoom, Long> {

    List<AIChatRoom> findAllByUserId(Long userId);

    Optional<AIChatRoom> findByIdAndUserId(Long chatRoomId, Long userId);

}
