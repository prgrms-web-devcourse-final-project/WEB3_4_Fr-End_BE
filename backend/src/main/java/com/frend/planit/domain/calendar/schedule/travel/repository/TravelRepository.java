package com.frend.planit.domain.calendar.schedule.travel.repository;

import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    // 특정 스케줄에 속한 모든 Travel 조회
    List<TravelEntity> findByScheduleId(Long scheduleId);
}
