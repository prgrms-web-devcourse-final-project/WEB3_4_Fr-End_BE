package com.frend.planit.domain.calendar.schedule.repository;

import com.frend.planit.domain.calendar.schedule.entity.ScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    // 특정 유저의 모든 일정
    @Query("""
                SELECT s
                FROM ScheduleEntity s
                JOIN s.calendar c
                JOIN c.user u
                WHERE u.id = :userId
            """)
    List<ScheduleEntity> findAllByUserId(@Param("userId") Long userId);
}
