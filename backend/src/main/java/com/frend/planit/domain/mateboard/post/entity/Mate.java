package com.frend.planit.domain.mateboard.post.entity;

import com.frend.planit.global.base.BaseTime;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * 메이트 모집 게시글을 나타내는 엔티티입니다. 사용자가 여행 동행자를 모집할 때 사용됩니다.
 * <p>게시글 수정 일자(modified_at)과 게시글 생성 날짜(created_at)는 BaseTime을 상속합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */
@Getter
@Setter
@Entity
public class Mate extends BaseTime {

    /**
     * 메이트 구함 게시글 ID는 BaseEntity 상속
     */

    /**
     * 사용자 ID (TODO: User 엔티티와 연관 관계 매핑 예정)
     *
     * @ManyToOne(fetch = FetchType.LAZY) private User id;
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 사용자 프로필 이미지 (TODO: User 엔티티와 연관 관계 매핑 예정)
     */
    private String profile_image;

    /**
     * 작성자 성별 (TODO: User 엔티티와 연관 관계 매핑 예정)
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * 게시글 제목
     */
    @Column(nullable = false)
    private String title;

    /**
     * 게시글 내용
     */
    @Column(nullable = false)
    private String content;

    /**
     * 여행 지역
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TravelRegion travelRegion;

    /**
     * 여행 시작 날짜
     */
    @Column(nullable = false)
    private LocalDate travelStartDate;

    /**
     * 여행 종료 날짜
     */
    @Column(nullable = false)
    private LocalDate travelEndDate;

    /**
     * 메이트 구함 상태
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus = RecruitmentStatus.OPEN;

    /**
     * 메이트 성별
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MateGender mateGender;

    /**
     * 게시글 생성 날짜(createdAt) BaseTime 상속
     */
    //

    /**
     * 게시글 수정 날짜(modified_at) BaseTime 상속
     */
    //

}

