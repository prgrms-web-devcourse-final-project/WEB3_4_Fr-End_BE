package com.frend.planit.domain.calendar.service;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.entity.SharedCalendarEntity;
import com.frend.planit.domain.calendar.repository.SharedCalendarRepository;
import com.frend.planit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedCalendarService {

    private final SharedCalendarRepository sharedCalendarRepository;

    @Transactional
    public void registerShare(CalendarEntity calendar, User receiver) {
        boolean alreadyShared = sharedCalendarRepository.existsByCalendarAndSharedUser(calendar, receiver);
        if (alreadyShared) return;

        SharedCalendarEntity shared = SharedCalendarEntity.create(calendar, receiver);
        sharedCalendarRepository.save(shared);
    }
}