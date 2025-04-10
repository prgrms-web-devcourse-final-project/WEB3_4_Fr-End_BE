package com.frend.planit.domain.mateboard.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 메이트 모집 신청의 상태를 나타내는 열거형(Enum)입니다.
 *
 * <p>총 세 가지 상태를 가질 수 있습니다:</p>
 * <ul>
 *     <li>{@code PENDING} - 수락 대기 중</li>
 *     <li>{@code ACCEPTED} - 신청이 수락됨</li>
 *     <li>{@code REJECTED} - 신청이 거절됨</li>
 * </ul>
 *
 * <p>{@code isAccepted()} 메서드를 통해 수락 여부를 쉽게 확인할 수 있습니다.</p>
 *
 * @author zelly
 * @since 2025-04-07
 */
@Getter
@RequiredArgsConstructor
public enum MateApplicationStatus {

    PENDING("대기"),
    ACCEPTED("수락"),
    REJECTED("거절");

    // 상태가 수락된 상태인지 판별하는 헬퍼 메서드
    public boolean isAccepted() {
        return this == ACCEPTED;
    }

    private final String displayName;
}
