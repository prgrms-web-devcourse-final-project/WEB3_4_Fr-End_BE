package com.frend.planit.domain.mateboard.comment.mapper;

import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.comment.entity.MateComment;

/**
 * MateComment 엔티티를 MateCommentResponseDto로 변환하는 매퍼 클래스입니다.
 * <p> 주로 메이트 모집 게시글을 조회하거나 목록으로 응답할 때 사용됩니다.</p>
 * <p> 정적 메서드 toResponseDto()를 통해 변환 작업을 수행하며, 이 메서드는 서비스 계층에서 호출됩니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-02
 */
public class MateCommentMapper {

    public static MateCommentResponseDto toResponseDto(MateComment comment) {
        return MateCommentResponseDto.builder()
                .matePostId(comment.getMate().getId())
                .userId(comment.getUser().getId())
                .nickname(comment.getUser().getNickname())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                // TODO: 좋아요 수 추가
                .build();
    }
}
