package com.frend.planit.domain.mateboard.comment.repository;

import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateCommentRepository extends JpaRepository<MateComment, Long> {

}
