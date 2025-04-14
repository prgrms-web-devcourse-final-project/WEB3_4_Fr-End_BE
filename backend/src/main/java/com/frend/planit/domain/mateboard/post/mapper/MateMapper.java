package com.frend.planit.domain.mateboard.post.mapper;

import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.entity.Mate;

/**
 * Mate 엔티티를 MateResponseDto로 변환하는 매퍼 클래스입니다.
 * <p> 주로 메이트 모집 게시글을 조회하거나 목록으로 응답할 때 사용됩니다.</p>
 * <p> 정적 메서드 toResponseDto()를 통해 변환 작업을 수행하며, 이 메서드는 서비스 계층에서 호출됩니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-31
 */
public class MateMapper {

    /**
     * Mate 엔티티를 MateResponseDto로 변환합니다.
     *
     * @param mate 변환할 Mate 엔티티
     * @return MateResponseDto 변환 결과
     */
    public static MateResponseDto toResponseDto(Mate mate, String imageUrl, boolean isApplied) {

        return MateResponseDto.builder()
                .matePostId(mate.getId())
                .authorId(mate.getWriter().getId())
                .nickname(mate.getWriter().getNickname())
                .profileImage(mate.getWriter().getProfileImageUrl())
                .authorGender(mate.getWriter().getGender())
                .bio(mate.getWriter().getBio())
                .title(mate.getTitle())
                .content(mate.getContent())
                .recruitCount(mate.getRecruitCount())
                .travelRegion(mate.getTravelRegion())
                .travelStartDate(mate.getTravelStartDate())
                .travelEndDate(mate.getTravelEndDate())
                .recruitmentStatus(mate.getRecruitmentStatus())
                .mateGender(mate.getMateGender())
                .appliedCount((int) mate.getApplications().stream()
                        .filter(a -> a.getStatus().isAccepted())
                        .count())
                .imageUrl(imageUrl)
                .createdAt(mate.getCreatedAt())
                .isApplied(isApplied)
                .build();
    }

    public static MateResponseDto toResponseDto(Mate mate, String imageUrl) {
        return toResponseDto(mate, imageUrl, false);
    }
}
