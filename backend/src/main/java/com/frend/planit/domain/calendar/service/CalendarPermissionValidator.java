package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.repository.SharedCalendarRepository;
import com.frend.planit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarPermissionValidator {

    private final SharedCalendarRepository sharedCalendarRepository;

    // 공유자 or 소유자 모두 접근 허용 (수정, 일정 등록)
    public boolean hasModifyAccess(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user)
                || sharedCalendarRepository.existsByCalendarAndSharedUser(calendar, user);
    }

    // 오직 소유자만 접근 허용 (삭제)
    public boolean isOwner(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user);
    }
}
