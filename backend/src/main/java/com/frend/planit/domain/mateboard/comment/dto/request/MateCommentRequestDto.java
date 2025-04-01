package com.frend.planit.domain.mateboard.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 메이트 모집 게시글에 댓글 작성, 수정 시에 사용되는 Request DTO입니다.
 * <p>작성자 정보와 댓글 내용을 포함하며, 유효성 검증을 수행합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-31
 */
@Getter
public class MateCommentRequestDto {

    private Long userId; // TODO: 로그인 연동 후 제거 예정

    private String nickname; // TODO: 로그인 연동 후 제거 예정

    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    private String content;
}
