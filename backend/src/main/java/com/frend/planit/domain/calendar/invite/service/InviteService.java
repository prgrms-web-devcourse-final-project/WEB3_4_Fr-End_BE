package com.frend.planit.domain.calendar.invite.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.invite.entity.InviteEntity;
import com.frend.planit.domain.calendar.invite.repository.InviteRepository;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public String create(Long calendarId) {
        CalendarEntity calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> ErrorType.CALENDAR_NOT_FOUND.serviceException());

        InviteEntity invite = InviteEntity.create(calendar);
        inviteRepository.save(invite);

        return invite.getInviteCode();
    }

    @Transactional
    public void invalidate(String inviteCode) {
        InviteEntity invite = inviteRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> ErrorType.INVITE_NOT_FOUND.serviceException());

        invite.invalidate();
        inviteRepository.save(invite);
    }

    @Transactional(readOnly = true)
    public InviteEntity findValidInvite(String inviteCode) {
        InviteEntity invite = inviteRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> ErrorType.INVITE_NOT_FOUND.serviceException());

        if (!invite.isValid()) {
            throw ErrorType.INVITE_INVALID.serviceException();
        }

        return invite;
    }
}