package com.frend.planit.domain.calendar.repository;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.calendar.entity.SharedCalendarEntity;
import com.frend.planit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedCalendarRepository extends JpaRepository<SharedCalendarEntity, Long> {

    List<SharedCalendarEntity> findAllBySharedUser(User sharedUser);

    Optional<SharedCalendarEntity> findByCalendarAndSharedUser(CalendarEntity calendar, User sharedUser);

    boolean existsByCalendarAndSharedUser(CalendarEntity calendar, User sharedUser);
}