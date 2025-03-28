package com.frend.planit.domain.board.mate.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDate;

/**
 * 메이트 모집 게시글을 나타내는 엔티티입니다. 사용자가 여행 동행자를 모집할 때 사용됩니다.
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-28
 */

@Entity
public class Mate { // TODO: extends BaseEntity

    /**
     * 메이트 구함 게시글 ID
     */
    @Id
    private Long matePostId;

    /**
     * 사용자 ID (TODO: User 엔티티와 연관 관계 매핑 예정)
     *
     * @ManyToOne(fetch = FetchType.LAZY) private User id;
     */
    @Column(name = "id")
    private Long userID;

    /**
     * 게시글 제목
     */
    private String title; // 제목
    
    /**
     * 게시글 내용
     */
    private String content;  // 내용

    /**
     * 여행 지역
     */
    @Enumerated(EnumType.STRING)
    private TravelRegion travelRegion;

    /**
     * 여행 시작 날짜
     */
    private LocalDate travelStartDate;

    /**
     * 여행 종료 날짜
     */
    private LocalDate travelEndDate;

    /**
     * 메이트 구함 상태
     */
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;

    /**
     * 메이트 성별
     */
    @Enumerated(EnumType.STRING)
    private MateGender mateGender;

    /**
     * 게시글 생성 날짜 TODO:createdAt은 BaseEntity에서 상속
     */
    //

}

