package com.frend.planit.domain.mateboard.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 메이트를 모집하는 글 작성자의 성별을 정의하는 Enum입니다. 여성(FEMALE), 남성(MALE) 2가지 항목이 존재합니다.
 * <p> 해당 Enum은 User 기능 구현 후 User와 연동할 후 삭제할 예정입니다. </p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-01
 */
@Getter
@RequiredArgsConstructor
public enum AuthorGender {

    FEMALE("여성"),
    MALE("남성");

    private final String displayName;

}
