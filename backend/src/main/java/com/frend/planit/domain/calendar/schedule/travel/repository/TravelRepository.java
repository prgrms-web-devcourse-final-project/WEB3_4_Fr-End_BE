package com.frend.planit.domain.calendar.schedule.travel.repository;

import com.frend.planit.domain.calendar.schedule.travel.entity.TravelEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    // 특정 스케줄에 속한 모든 Travel 조회
    @Query("""
            SELECT t FROM TravelEntity t
            JOIN FETCH t.scheduleDay sd
            WHERE sd.schedule.id = :scheduleId
            ORDER BY sd.date ASC, t.visitHour ASC, t.visitMinute ASC
            """)
    List<TravelEntity> findAllByScheduleId(@Param("scheduleId") Long scheduleId);
}
