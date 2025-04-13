package com.frend.planit.domain.mateboard.application.controller;

import com.frend.planit.domain.mateboard.application.service.MateApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메이트 신청 관련 요청을 처리하는 컨트롤러입니다.
 * <p>사용자는 게시글에 신청하거나, 신청을 취소할 수 있으며,</p>
 * <p>게시글 작성자는 신청자를 수락 또는 거절할 수 있습니다.</p>
 *
 * @author zelly
 * @since 2025-04-07
 */
@RequestMapping("/api/v1/mate-board/applications")
@RequiredArgsConstructor
@RestController
public class MateApplicationController {

    private final MateApplicationService mateApplicationService;

    /**
     * 메이트 모집 게시글에 신청합니다.
     *
     * @param matePostId 신청할 게시글 ID
     * @param userId     현재 로그인한 사용자
     */
    @PostMapping("/{matePostId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void applyToMate(@PathVariable Long matePostId, @AuthenticationPrincipal Long userId) {
        mateApplicationService.applyToMate(userId, matePostId);
    }

    /**
     * 메이트 모집 게시글에 대한 신청을 취소합니다.
     *
     * @param matePostId 신청을 취소할 게시글 ID
     * @param userId     현재 로그인한 사용자 (신청자 본인)
     */
    @DeleteMapping("/{matePostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelApplication(@PathVariable Long matePostId,
            @AuthenticationPrincipal Long userId) {
        mateApplicationService.cancelApplication(userId, matePostId);
    }

    /**
     * 게시글 작성자가 특정 신청자의 신청을 수락합니다.
     *
     * @param matePostId  게시글 ID
     * @param applicantId 수락할 신청자 ID
     * @param userId      현재 로그인한 사용자 (게시글 작성자)
     */
    @PutMapping("/{matePostId}/accept/{applicantId}")
    @ResponseStatus(HttpStatus.OK)
    public void acceptApplication(
            @PathVariable Long matePostId,
            @PathVariable Long applicantId,
            @AuthenticationPrincipal Long userId) {
        mateApplicationService.acceptApplication(matePostId, applicantId, userId);
    }

    /**
     * 게시글 작성자가 특정 신청자의 신청을 거절합니다.
     *
     * @param matePostId  게시글 ID
     * @param applicantId 거절할 신청자 ID
     * @param userId      현재 로그인한 사용자 (게시글 작성자)
     */
    @PutMapping("/{matePostId}/reject/{applicantId}")
    @ResponseStatus(HttpStatus.OK)
    public void rejectApplication(
            @PathVariable Long matePostId,
            @PathVariable Long applicantId,
            @AuthenticationPrincipal Long userId) {
        mateApplicationService.rejectApplication(matePostId, applicantId, userId);
    }
}
