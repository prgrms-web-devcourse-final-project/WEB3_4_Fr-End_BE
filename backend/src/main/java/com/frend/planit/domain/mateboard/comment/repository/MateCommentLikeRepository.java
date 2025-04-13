package com.frend.planit.domain.mateboard.comment.repository;

import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.comment.entity.MateCommentLike;
import com.frend.planit.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateCommentLikeRepository extends JpaRepository<MateCommentLike, Long> {

    Optional<MateCommentLike> findByUserAndMateComment(User user, MateComment mateComment);

    Long countByMateComment(MateComment mateComment);
}
