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

    // 캘린더에 관한 수정관한여부 확인
    public boolean hasModifyPermission(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user) ||
                sharedCalendarRepository.existsByCalendarAndSharedUser(calendar, user);
    }

    // 최초 생성자만 캘린더 삭제가능
    public boolean isOwner(CalendarEntity calendar, User user) {
        return calendar.getUser().equals(user);
    }
}