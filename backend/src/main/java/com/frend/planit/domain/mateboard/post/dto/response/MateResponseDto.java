package com.frend.planit.domain.mateboard.post.dto.response;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 메이트 모집 게시글 조회, 목록 응답 시에 사용하는 Response DTO입니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
@Getter
@Builder
@AllArgsConstructor
public class MateResponseDto {

    private final Long id;

    private final String title;

    private final String content;

    private final TravelRegion travelRegion;

    private final LocalDate travelStartDate;

    private final LocalDate travelEndDate;

    private final RecruitmentStatus recruitmentStatus;

    private final MateGender mateGender;

    private LocalDateTime createdAt;

}
