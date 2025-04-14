package com.frend.planit.domain.mateboard.comment.controller;

import com.frend.planit.domain.mateboard.comment.service.MateCommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mate-board/posts/{matePostId}/comments/")
public class MateCommentLikeController {

    private final MateCommentLikeService mateCommentLikeService;

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId,
            @AuthenticationPrincipal Long userId) {
        mateCommentLikeService.like(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId,
            @AuthenticationPrincipal Long userId) {
        mateCommentLikeService.unlike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
