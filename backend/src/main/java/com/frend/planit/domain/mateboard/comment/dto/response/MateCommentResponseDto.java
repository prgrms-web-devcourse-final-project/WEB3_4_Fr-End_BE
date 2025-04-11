package com.frend.planit.domain.mateboard.comment.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 메이트 모집 게시글에 댓글 조회, 삭제 시에 사용되는 Response DTO입니다.
 * <p>작성자 정보, 댓글 내용, 생성 및 수정 시각을 포함합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-01
 */
@Getter
@Builder
public class MateCommentResponseDto {

    private final Long commentId;

    private final Long matePostId;

    private final Long authorId;

    private final String nickname;

    private final String profileImageUrl;

    private final String content;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
