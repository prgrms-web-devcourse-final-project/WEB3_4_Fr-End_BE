package com.frend.planit.domain.calendar.entity;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SharedCalendarEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private CalendarEntity calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User sharedUser;

    @Column(nullable = false)
    private LocalDateTime sharedAt;

    public static SharedCalendarEntity create(CalendarEntity calendar, User sharedUser) {
        return SharedCalendarEntity.builder()
                .calendar(calendar)
                .sharedUser(sharedUser)
                .sharedAt(LocalDateTime.now())
                .build();
    }
}