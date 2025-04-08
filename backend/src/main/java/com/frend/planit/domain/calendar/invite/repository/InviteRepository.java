package com.frend.planit.domain.calendar.invite.repository;

import com.frend.planit.domain.calendar.invite.entity.InviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepository extends JpaRepository<InviteEntity, Long> {

    Optional<InviteEntity> findByInviteCode(String inviteCode);
}