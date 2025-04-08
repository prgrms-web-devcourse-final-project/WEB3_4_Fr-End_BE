package com.frend.planit.domain.chatbot.repository;

import com.frend.planit.domain.chatbot.entity.AIChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIChatMessageRepository extends JpaRepository<AIChatMessage, Long> {

}
