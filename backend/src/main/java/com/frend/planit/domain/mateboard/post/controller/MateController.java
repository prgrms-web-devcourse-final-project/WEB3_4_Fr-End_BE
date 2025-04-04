package com.frend.planit.domain.mateboard.post.controller;

import com.frend.planit.domain.mateboard.post.dto.request.MateRequestDto;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.service.MateService;
import com.frend.planit.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
 * 메이트 모집 게시글 관련 요청을 처리하는 REST 컨트롤러입니다.
 * <p> 게시글 생성, 단건 조회, 전체 조회, 수정, 삭제 등의 CRUD 기능을 제공합니다.</p>
 * <p> Service를 통해 비즈니스 로직을 처리하며, 추후 상태 자동 변경 등 확장 기능도 이곳에서 연결됩니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mate-board/posts")
public class MateController {

    private final MateService mateService;

    /**
     * 메이트 모집 게시글을 생성합니다.
     *
     * @param mateRequestDto 사용자가 입력한 게시글 정보
     * @return mateId 생성된 게시글 ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMate(@RequestBody @Valid MateRequestDto mateRequestDto) {
        // TODO: 로그인 기능 연동 필요
        Long userId = 1L; // 로그인 기능 연동 전까지 임시 값
        return mateService.createMate(userId, mateRequestDto);
    }

    /**
     * 메이트 모집 게시글 전체 조회합니다.
     *
     * @param pageable 페이징 정보 (페이지 번호, 한 페이지당 게시글 수: 8개, 정렬 기준: createdAt, 정렬 순서: 내림차순(DESC))
     * @return mates 페이징 된 게시글 목록
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<MateResponseDto> getAllMates(
            @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return mateService.findAllWithPaging(pageable);
    }

    /**
     * 메이트 모집 게시글 단건 조회합니다.
     *
     * @param id 조회할 게시글 ID
     * @return 조회된 된 게시글 id
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MateResponseDto getMate(@PathVariable Long id) {
        return mateService.getMate(id);

    }

    /**
     * 메이트 모집 게시글을 수정합니다.
     *
     * @param id             수정할 게시글 ID
     * @param mateRequestDto 수정할 게시글의 요청 본문(제목, 내용, 날짜, 지역, 성별 등)
     * @return 수정된 게시글 id
     */
    @ResponseStatus(HttpStatus.OK)
    public MateResponseDto updateMate(@PathVariable Long id,
            @RequestBody @Valid MateRequestDto mateRequestDto) {
        MateResponseDto updatedMate = mateService.updateMate(id, mateRequestDto);
        return updatedMate;
    }

    /**
     * 메이트 모집 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글 ID
     * @return 삭제한 게시글 응답 DTO
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMate(@PathVariable Long id) {
        mateService.deleteMate(id);
    }
}