package com.frend.planit.domain.calendar.repository;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
}