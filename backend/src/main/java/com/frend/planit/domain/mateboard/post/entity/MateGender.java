package com.frend.planit.domain.mateboard.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 모집 메이트의 성별을 정의하는 Enum입니다. 여성(FEMALE), 남성(MALE), 성별 무관(NO_PREFERENCE) 3가지 항목이 존재합니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
@Getter
@RequiredArgsConstructor
public enum MateGender {

    NO_PREFERENCE("성별 무관"),
    FEMALE("여성"),
    MALE("남성");

    private final String displayName;

}
