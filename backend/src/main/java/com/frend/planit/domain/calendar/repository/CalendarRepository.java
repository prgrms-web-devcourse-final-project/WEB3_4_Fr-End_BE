package com.frend.planit.domain.calendar.repository;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    CalendarEntity findByUserId(Long userId);

    List<CalendarEntity> findAllByUser(User user);
}