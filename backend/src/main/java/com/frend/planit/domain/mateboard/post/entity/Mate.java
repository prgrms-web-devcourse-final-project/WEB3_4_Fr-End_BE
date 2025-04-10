package com.frend.planit.domain.mateboard.post.entity;

import com.frend.planit.domain.mateboard.application.entity.MateApplication;
import com.frend.planit.domain.mateboard.application.entity.MateApplicationStatus;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "mate")
@AttributeOverride(name = "id", column = @Column(name = "mate_post_id"))
public class Mate extends BaseTime {

    /**
     * 메이트 구함 게시글 ID는 BaseEntity 상속
     */

    /**
     * 사용자 ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    /**
     * 사용자 프로필 이미지
     */

    /**
     * 사용자 닉네임
     */

    /**
     * 작성자 성별
     */

    /**
     * 작성자 프로필 이미지
     */

    /**
     * 게시글에 등록된 이미지 (최대 1장)
     */

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
     * 메이트 모집 인원
     */
    @Column(nullable = false)
    private int recruitCount;

    /**
     * 메이트 신청 목록
     */
    @OneToMany(mappedBy = "mate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MateApplication> applications = new ArrayList<>();

    /**
     * 게시글 생성 날짜(createdAt), 게시글 수정 날짜(modifiedAt) BaseTime 상속
     */
    //
    public void closeRecruitmentIfFull() {
        long acceptedCount = this.applications.stream().filter(app -> app.getStatus()
                == MateApplicationStatus.ACCEPTED).count();
        if (acceptedCount >= this.recruitCount) {
            this.recruitmentStatus = RecruitmentStatus.CLOSED;
        }
    }

    public boolean isRecruiting() {
        return this.recruitmentStatus == RecruitmentStatus.OPEN;
    }

}
