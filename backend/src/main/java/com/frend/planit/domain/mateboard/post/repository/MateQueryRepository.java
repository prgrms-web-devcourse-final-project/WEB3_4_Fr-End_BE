package com.frend.planit.domain.mateboard.post.repository;

import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MateQueryRepository {

    /**
     * 검색 조건(keyword, status, region)에 따라 메이트 게시글을 조회합니다. 페이징 처리를 포함하며, 검색 조건이 없을 경우 전체 조회와 동일합니다.
     *
     * @param keyword  제목, 내용, 작성자 검색 키워드
     * @param status   모집 상태 (OPEN, CLOSED, ALL 등)
     * @param region   여행 지역 (서울, 부산 등)
     * @param pageable 페이징 및 정렬 정보
     * @return 필터링된 게시글의 페이지 응답
     */
    Page<MateResponseDto> searchMatePosts(String keyword, String status, String region,
            Pageable pageable);

}
