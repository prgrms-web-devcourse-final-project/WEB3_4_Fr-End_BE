package com.frend.planit.domain.mateboard.post.dto.response;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class MateResponseDto {

    private Long id;

    private String title;

    private String content;

    private TravelRegion travelRegion;

    private LocalDate travelStartDate;

    private LocalDate travelEndDate;

    private RecruitmentStatus recruitmentStatus;

    private MateGender mateGender;

    private LocalDateTime createdAt;

}
