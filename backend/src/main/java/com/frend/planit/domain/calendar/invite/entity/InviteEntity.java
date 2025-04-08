package com.frend.planit.domain.calendar.invite.entity;

import com.frend.planit.domain.calendar.entity.CalendarEntity;
import com.frend.planit.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InviteEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private CalendarEntity calendar;

    @Column(unique = true, nullable = false, length = 100)
    private String inviteCode;

    @Column(nullable = false)
    private boolean valid;

    public static InviteEntity create(CalendarEntity calendar) {
        return InviteEntity.builder()
                .calendar(calendar)
                .inviteCode(UUID.randomUUID().toString())
                .valid(true)
                .build();
    }

    public void invalidate() {
        this.valid = false;
    }
}