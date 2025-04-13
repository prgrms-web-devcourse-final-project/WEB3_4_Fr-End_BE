package com.frend.planit.domain.mateboard.comment.entity;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "mate_comment_like_id"))
public class MateCommentLike extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mate_comment_id", nullable = false)
    private MateComment mateComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public MateCommentLike(MateComment mateComment, User user) {
        this.mateComment = mateComment;
        this.user = user;
    }

}
