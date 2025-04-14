package com.frend.planit.domain.mateboard.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 동행 신청 정보를 담는 DTO입니다.
 * <p>작성자(authorId)가 어떤 동행 신청(applicationId)을 했는지 나타냅니다.</p>
 */
@Getter
@AllArgsConstructor
public class MateApplicationInfo {

    private Long authorId;
    private Long applicationId;
}
