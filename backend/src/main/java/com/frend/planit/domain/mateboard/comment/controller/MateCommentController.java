package com.frend.planit.domain.mateboard.comment.controller;

import com.frend.planit.domain.mateboard.comment.dto.request.MateCommentRequestDto;
import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.comment.service.MateCommentService;
import com.frend.planit.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메이트 모집 게시글 댓글 관련 요청을 처리하는 REST 컨트롤러입니다.
 * <p> 댓글 생성, 단건 조회, 전체 조회, 수정, 삭제 등의 CRUD 기능을 제공합니다.</p>
 * <p> Service를 통해 비즈니스 로직을 처리하며, 추후 상태 자동 변경 등 확장 기능도 이곳에서 연결됩니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mate-board/posts/{matePostId}/comments")
public class MateCommentController {

    private final MateCommentService mateCommentService;

    /**
     * 메이트 모집 게시글에 댓글을 생성합니다.
     *
     * @param mateCommentRequestDto 사용자가 입력한 댓글 정보
     * @param mateId                댓글이 달린 게시글 ID
     * @return 생성된 댓글 ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createComment(
            @RequestBody @Valid MateCommentRequestDto mateCommentRequestDto,
            @PathVariable Long matePostId,
            @AuthenticationPrincipal Long userId) {
        return mateCommentService.createComment(userId, matePostId, mateCommentRequestDto);
    }

    /**
     * 특정 댓글을 조회합니다.
     *
     * @param id 조회할 댓글 ID
     * @return 조회된 댓글 ID
     */
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public MateCommentResponseDto getComment(@PathVariable Long commentId) {
        return mateCommentService.getComment(commentId);
    }

    /**
     * 특정 메이트 모집 게시글에 댓글을 전체 조회합니다.
     *
     * @param mateId   메이트 모집 게시글 ID
     * @param pageable 페이징 정보 (페이지 번호, 한 페이지당 게시글 수: 5개, 정렬 기준: createdAt, 정렬 순서: 내림차순(DESC))
     * @return 페이징 된 댓글 목록
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<MateCommentResponseDto> getAllComments(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
            @PathVariable Long matePostId, Pageable pageable) {
        return mateCommentService.findAllByMateIdWithPaging(matePostId, pageable);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param id                    수정할 댓글 ID
     * @param mateCommentRequestDto 수정할 댓글 요청 본문
     * @return 수정된 댓글 ID
     */
    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public MateCommentResponseDto updateComment(@PathVariable Long commentId,
            @RequestBody @Valid MateCommentRequestDto mateCommentRequestDto,
            @AuthenticationPrincipal Long userId) {
        return mateCommentService.updateComment(commentId, mateCommentRequestDto, userId);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param id
     * @param userId
     * @return
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MateCommentResponseDto deleteComment(@PathVariable Long commentId,
            @AuthenticationPrincipal Long userId) {
        return mateCommentService.deleteComment(commentId, userId);
    }
}
