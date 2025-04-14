package com.frend.planit.domain.mateboard.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 게시글 좋아요 정보를 담는 DTO입니다.
 * <p>작성자(authorId)가 어떤 게시글(matePostId)에 좋아요를 눌렀는지 나타냅니다.</p>
 */
@Getter
@AllArgsConstructor
public class PostLikeInfo {

    private Long authorId;
    private Long matePostId;
}