package com.frend.planit.domain.mateboard.comment.repository;

import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateCommentRepository extends JpaRepository<MateComment, Long> {

    Page<MateComment> findAllByMate(Mate mate, Pageable pageable);

    List<MateComment> findByUserId(Long userId);
}
