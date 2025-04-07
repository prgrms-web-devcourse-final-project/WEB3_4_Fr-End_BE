package com.frend.planit.domain.mateboard.application.entity;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메이트 모집 게시글에 대한 사용자의 신청 정보를 저장하는 엔티티입니다.
 *
 * <p>사용자(User)와 메이트 게시글(Mate) 간의 다대다 관계를 매핑하며, 신청 상태를 포함합니다.</p>
 *
 * <ul>
 *     <li>{@code applicant}: 신청한 사용자</li>
 *     <li>{@code mate}: 신청 대상 메이트 게시글</li>
 *     <li>{@code status}: 신청 상태(PENDING, ACCEPTED, REJECTED)</li>
 * </ul>
 *
 * <p>{@code accept()}, {@code reject()}, {@code updateStatus()} 등의 메서드를 통해 상태를 명확하게 제어할 수 있습니다.</p>
 * <p>{@code @PrePersist}를 통해 신청 상태가 지정되지 않은 경우 자동으로 {@code PENDING} 상태로 설정됩니다.</p>
 *
 * @author zelly
 * @since 2025-04-07
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateApplication extends BaseTime {

    // 지원자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    // 지원한 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_id", nullable = false)
    private Mate mate;

    // 신청 상태 (대기 / 수락 / 거절)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MateApplicationStatus status;

    // 생성 시 기본 PENDING
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = MateApplicationStatus.PENDING;
        }
    }

    // 상태 변경 메서드 (수락)
    public void accept() {
        this.status = MateApplicationStatus.ACCEPTED;
    }

    // 상태 변경 메서드 (거절)
    public void reject() {
        this.status = MateApplicationStatus.REJECTED;
    }
}
