package com.frend.planit.domain.mateboard.post.entity;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메이트 모집 게시글의 좋아요를 나타내는 엔티티입니다. 사용자가 메이트 모집 게시글에 좋아요를 할 때 사용됩니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-13
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mate_post_like")
@AttributeOverride(name = "id", column = @Column(name = "mate_post_like_id"))
public class MatePostLike extends BaseTime {

    /**
     * 메이트 모집 게시글 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_post_id", nullable = false)
    private Mate matePost;

    /**
     * 사용자 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 게시글 좋아요 갯수
     */
    public MatePostLike(Mate matePost, User user) {
        this.matePost = matePost;
        this.user = user;
    }
}
