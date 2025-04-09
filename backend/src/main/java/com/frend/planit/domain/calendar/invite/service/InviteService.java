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

import java.util.Optional;

import static com.frend.planit.global.response.ErrorType.*;

@Service
@RequiredArgsConstructor
public class InviteService {

    private static <T> T orThrow(Optional<T> optional, ErrorType errorType) {
        return optional.orElseThrow(() -> errorType.serviceException());
    }

    private final InviteRepository inviteRepository;
    private final CalendarRepository calendarRepository;
    private final SharedCalendarService sharedCalendarService;

    // 초대 코드 생성
    @Transactional
    public String create(Long calendarId) {
        CalendarEntity calendar = orThrow(calendarRepository.findById(calendarId), CALENDAR_NOT_FOUND);
        InviteEntity invite = InviteEntity.create(calendar);
        inviteRepository.save(invite);
        return invite.getInviteCode();
    }

    // 생성된 초대 코드 무효화
    @Transactional
    public void invalidate(String inviteCode) {
        InviteEntity invite = orThrow(inviteRepository.findByInviteCode(inviteCode), INVITE_NOT_FOUND);
        invite.invalidate();
        inviteRepository.save(invite);
    }

    // 초대 링크(코드)를 통해 캘린더 공유처리
    @Transactional
    public void shareCalendarByInvite(String inviteCode, User receiver) {
        InviteEntity invite = orThrow(inviteRepository.findByInviteCode(inviteCode), INVITE_NOT_FOUND);

        if (!invite.isValid()) {
            throw INVITE_INVALID.serviceException();
        }

        sharedCalendarService.registerShare(invite.getCalendar(), receiver);
    }
}