package com.frend.planit.domain.mateboard.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 댓글 좋아요 정보를 담는 DTO입니다.
 * <p>authorId(사용자)와 commentId(댓글 ID)를 포함합니다.</p>
 */
@Getter
@AllArgsConstructor
public class CommentLikeInfo {

    private Long authorId;
    private Long commentId;
}