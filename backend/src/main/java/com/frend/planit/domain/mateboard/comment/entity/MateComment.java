package com.frend.planit.domain.mateboard.comment.entity;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * 메이트 모집 게시판 댓글을 나타내는 엔티티입니다.
 * <p>댓글 수정 일자(modified_at)과 댓글 생성 날짜(created_at)는 BaseTime을 상속합니다.</p>
 * <p>
 * 댓글은 특정 메이트 모집 게시글에 작성됩니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-31
 */
@Entity
public class MateComment extends BaseTime {

    /**
     * 메이트 모집 댓글 ID는 BaseEntity 상속
     */

    /**
     * 메이트 모집 게시글 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_post_id", nullable = false)
    private Mate mate;

    /**
     * 사용자 ID (TODO: User 엔티티와 연관 관계 매핑 예정)
     *
     * @ManyToOne(fetch = FetchType.LAZY) private User id;
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 사용자 nickname (TODO: User 엔티티와 연관 관계 매핑 예정)
     */
    @Column(nullable = false)
    private String nickname;

    /**
     * 사용자 프로필 이미지 (TODO: User 엔티티와 연관 관계 매핑 예정)
     */
    private String profileImageUrl;

    /**
     * 댓글 내용
     */
    @Column(nullable = false)
    private String content;

    /**
     * 댓글 생성 날짜(createdAt) BaseTime 상속
     */
    //

    /**
     * 댓글 수정 날짜(modified_at) BaseTime 상속
     */
    //
}
