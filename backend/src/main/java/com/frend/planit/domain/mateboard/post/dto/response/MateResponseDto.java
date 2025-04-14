package com.frend.planit.domain.mateboard.post.dto.response;

import com.frend.planit.domain.mateboard.post.entity.MateGender;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.entity.TravelRegion;
import com.frend.planit.domain.user.enums.Gender;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 메이트 모집 게시글 조회, 목록 응답 시에 사용하는 Response DTO입니다.
 *
 * <p>상세 조회 시 postLike, mateApplications, isApplied 등의 정보를 포함합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
@Getter
public class MateResponseDto {

    private final Long matePostId;
    private final Long authorId;
    private final String title;
    private final String content;
    private final TravelRegion travelRegion;
    private final LocalDate travelStartDate;
    private final LocalDate travelEndDate;
    private final RecruitmentStatus recruitmentStatus;
    private final MateGender mateGender;
    private final int recruitCount;
    private final int appliedCount;
    private final String imageUrl;
    private final String nickname;
    private final String bio;
    private final String profileImage;
    private final Gender authorGender;
    private final int commentCount;
    private final int likeCount;
    private final LocalDateTime createdAt;
    private final boolean isApplied;

    private final List<PostLikeInfo> postLike;
    private final List<MateApplicationInfo> mateApplications;

    @QueryProjection
    public MateResponseDto(Long matePostId, Long authorId, String title, String content,
            TravelRegion travelRegion, LocalDate travelStartDate, LocalDate travelEndDate,
            RecruitmentStatus recruitmentStatus, MateGender mateGender,
            int recruitCount, int appliedCount, String imageUrl,
            String nickname, String bio, String profileImage, Gender authorGender,
            int commentCount, int likeCount, LocalDateTime createdAt, boolean isApplied) {
        this.matePostId = matePostId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.travelRegion = travelRegion;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.mateGender = mateGender;
        this.recruitCount = recruitCount;
        this.appliedCount = appliedCount;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.bio = bio;
        this.profileImage = profileImage;
        this.authorGender = authorGender;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.isApplied = isApplied;
        this.postLike = null;
        this.mateApplications = null;
    }

    @Builder
    public MateResponseDto(Long matePostId, Long authorId, String title, String content,
            TravelRegion travelRegion, LocalDate travelStartDate, LocalDate travelEndDate,
            RecruitmentStatus recruitmentStatus, MateGender mateGender,
            int recruitCount, int appliedCount, String imageUrl,
            String nickname, String bio, String profileImage, Gender authorGender,
            int commentCount, int likeCount, LocalDateTime createdAt,
            boolean isApplied, List<PostLikeInfo> postLike,
            List<MateApplicationInfo> mateApplications) {
        this.matePostId = matePostId;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.travelRegion = travelRegion;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.mateGender = mateGender;
        this.recruitCount = recruitCount;
        this.appliedCount = appliedCount;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.bio = bio;
        this.profileImage = profileImage;
        this.authorGender = authorGender;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.isApplied = isApplied;
        this.postLike = postLike;
        this.mateApplications = mateApplications;
    }
}