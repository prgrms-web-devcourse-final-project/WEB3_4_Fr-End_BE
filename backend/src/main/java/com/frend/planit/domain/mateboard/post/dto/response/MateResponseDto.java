package com.frend.planit.domain.mateboard.post.dto.response;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.user.enums.Gender;
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

    private final String nickname;

    private final String profileImage;

    private final Gender authorGender;

    private final String title;

    private final String content;

    private final TravelRegion travelRegion;

    private final LocalDate travelStartDate;

    private final LocalDate travelEndDate;

    private final int recruitCount;

    private final int appliedCount;

    private final RecruitmentStatus recruitmentStatus;

    private final MateGender mateGender;

    private final String imageUrl;

    private LocalDateTime createdAt;

    public MateResponseDto(Long id, String title, String content, TravelRegion travelRegion,
            LocalDate travelStartDate, LocalDate travelEndDate, RecruitmentStatus recruitmentStatus,
            MateGender mateGender, Gender authorGender, int recruitCount, int appliedCount,
            String nickname,
            String profileImage, String imageUrl,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.travelRegion = travelRegion;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.mateGender = mateGender;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.authorGender = authorGender;
        this.recruitCount = recruitCount;
        this.appliedCount = appliedCount;
        this.imageUrl = imageUrl;

    }
}
