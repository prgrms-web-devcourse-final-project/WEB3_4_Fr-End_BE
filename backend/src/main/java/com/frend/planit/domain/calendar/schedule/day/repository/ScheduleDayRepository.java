package com.frend.planit.domain.calendar.schedule.day.repository;

import com.frend.planit.domain.calendar.schedule.day.entity.ScheduleDayEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDayEntity, Long> {

    List<ScheduleDayEntity> findAllByScheduleId(Long scheduleId);
}