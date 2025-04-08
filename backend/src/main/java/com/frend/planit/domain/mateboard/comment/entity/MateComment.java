package com.frend.planit.domain.mateboard.comment.entity;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * 메이트 모집 게시판 댓글을 나타내는 엔티티입니다. 댓글은 특정 메이트 모집 게시글에 작성됩니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-31
 */
@Getter
@Setter
@Entity
public class MateComment extends BaseTime {

    /**
     * 메이트 모집 게시글 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_id", nullable = false)
    private Mate mate;

    /**
     * 사용자 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 댓글 내용
     */
    @Column(nullable = false)
    private String content;

}
