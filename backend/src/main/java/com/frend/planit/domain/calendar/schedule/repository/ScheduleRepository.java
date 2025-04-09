package com.frend.planit.domain.calendar.schedule.repository;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByCalendarId(Long id);
}
