package com.frend.planit.domain.calendar.schedule.repository;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    // 스케줄 존재 여부 확인
    @Query("SELECT s FROM ScheduleEntity s WHERE s.id = :scheduleId AND s.calendar.id = :calendarId")
    Optional<ScheduleEntity> findByIdAndCalendarId(
            @Param("calendarId") Long calendarId,
            @Param("scheduleId") Long scheduleId);

    // 특정 유저의 모든 일정
    @Query("""
                SELECT DISTINCT s FROM ScheduleEntity s
                JOIN FETCH s.scheduleDayList d
                LEFT JOIN FETCH d.travelList t
                WHERE s.calendar.user.id = :userId
            """)
    List<ScheduleEntity> findAllByUserId(@Param("userId") Long userId);
}
