package com.frend.planit.domain.mateboard.post.controller;

import com.frend.planit.domain.mateboard.post.service.MatePostLikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/mate-board/posts/")
public class MatePostLikeController {

    private final MatePostLikeService matePostLikeService;

    @PostMapping("{matePostId}/like")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> like(@PathVariable Long matePostId,
            @AuthenticationPrincipal Long userId) {
        matePostLikeService.like(matePostId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{matePostId}/like")
    public ResponseEntity<Void> unlike(@PathVariable Long matePostId,
            @AuthenticationPrincipal Long userId) {
        matePostLikeService.unLike(matePostId, userId);
        return ResponseEntity.ok().build();
    }
}
