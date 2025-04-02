package com.frend.planit.domain.mateboard.comment.controller;

import com.frend.planit.domain.mateboard.comment.dto.request.MateCommentRequestDto;
import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.comment.service.MateCommentService;
import com.frend.planit.global.response.ApiResponseHelper;
import com.frend.planit.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/v1/mate-board/posts/{mateId}/comments")
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
    public ResponseEntity<Long> createComment(
            @RequestBody @Valid MateCommentRequestDto mateCommentRequestDto,
            @PathVariable Long mateId) {
        // TODO: 로그인 기능 연동 필요
        Long userId = 1L;
        Long commentId = mateCommentService.createComment(userId, mateId, mateCommentRequestDto);
        return ApiResponseHelper.success(HttpStatus.CREATED, commentId);
    }

    /**
     * 특정 댓글을 조회합니다.
     *
     * @param id 조회할 댓글 ID
     * @return 조회된 댓글 ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MateCommentResponseDto> getComment(@PathVariable Long id) {
        MateCommentResponseDto comment = mateCommentService.getComment(id);
        return ApiResponseHelper.success(HttpStatus.OK, comment);
    }

    /**
     * 특정 메이트 모집 게시글에 댓글을 전체 조회합니다.
     *
     * @param mateId   메이트 모집 게시글 ID
     * @param pageable 페이징 정보 (페이지 번호, 한 페이지당 게시글 수: 5개, 정렬 기준: createdAt, 정렬 순서: 내림차순(DESC))
     * @return 페이징 된 댓글 목록
     */
    @GetMapping
    public ResponseEntity<PageResponse<MateCommentResponseDto>> getAllComments(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC)
            @PathVariable Long mateId, Pageable pageable) {
        PageResponse<MateCommentResponseDto> comments = mateCommentService.
                findAllByMateIdWithPaging(mateId, pageable);
        return ApiResponseHelper.success(HttpStatus.OK, comments);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param id                    수정할 댓글 ID
     * @param mateCommentRequestDto 수정할 댓글 요청 본문
     * @return 수정된 댓글 ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<MateCommentResponseDto> updateComment(@PathVariable Long id,
            @RequestBody @Valid MateCommentRequestDto mateCommentRequestDto) {
        MateCommentResponseDto updateComment = mateCommentService.updateComment(id,
                mateCommentRequestDto);
        return ApiResponseHelper.success(HttpStatus.OK, updateComment);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param id 삭제할 댓글 ID
     * @return 삭제된 댓글 ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MateCommentResponseDto> deleteComment(@PathVariable Long id) {
        MateCommentResponseDto deleteComment = mateCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
