package com.frend.planit.domain.board.mate.post.entity;

/**
 * 모집 상태를 정의하는 Enum입니다. 모집 중(OPEN), 모집 종료(CLOSE) 2가지 상태가 존재합니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
public enum RecruitmentStatus {
    OPEN("모집 중"),
    CLOSED("모집 종료");

    private final String label;

    RecruitmentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
