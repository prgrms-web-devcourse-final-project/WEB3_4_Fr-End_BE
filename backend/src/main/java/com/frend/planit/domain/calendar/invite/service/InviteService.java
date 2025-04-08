package com.frend.planit.domain.calendar.invite.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.invite.entity.InviteEntity;
import com.frend.planit.domain.calendar.invite.repository.InviteRepository;
import com.frend.planit.domain.calendar.repository.CalendarRepository;
import com.frend.planit.domain.calendar.service.SharedCalendarService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final CalendarRepository calendarRepository;
    private final SharedCalendarService sharedCalendarService;

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

    /**
     * 초대 링크 클릭 시 실행: 공유 등록
     */
    @Transactional
    public void shareCalendarByInvite(String inviteCode, User receiver) {
        InviteEntity invite = inviteRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> ErrorType.INVITE_NOT_FOUND.serviceException());

        if (!invite.isValid()) {
            throw ErrorType.INVITE_INVALID.serviceException();
        }

        sharedCalendarService.registerShare(invite.getCalendar(), receiver);
    }
}