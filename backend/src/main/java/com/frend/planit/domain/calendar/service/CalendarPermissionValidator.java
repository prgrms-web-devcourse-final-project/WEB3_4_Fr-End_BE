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

    // 캘린더 수정 및 등록
    public boolean hasModifyAccess(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user)
                || sharedCalendarRepository.existsByCalendarAndSharedUser(calendar, user);
    }

    // 캘린더 삭제 - 생성자만 가능
    public boolean isOwner(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user);
    }
}