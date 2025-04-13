package com.frend.planit.domain.mateboard.post.repository;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.MatePostLike;
import com.frend.planit.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatePostLikeRepository extends JpaRepository<MatePostLike, Long> {

    // 사용자가 해당 게시글에 좋아요 눌렀는지 확인
    Optional<MatePostLike> findByUserAndMatePost(User user, Mate mate);

    // 해당 게시글에 눌린 좋아요 수 카운트
    Long countByMatePost(Mate mate);
    
}
