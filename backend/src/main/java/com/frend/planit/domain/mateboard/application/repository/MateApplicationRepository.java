package com.frend.planit.domain.mateboard.application.repository;

import com.frend.planit.domain.mateboard.application.entity.MateApplication;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 메이트 신청(MateApplication)에 대한 데이터베이스 접근을 처리하는 Repository 인터페이스입니다.
 *
 * <p>기본 CRUD 기능은 JpaRepository를 통해 제공되며,</p>
 * <p>추가로 게시글 ID와 신청자 ID로 신청 내역을 조회하는 커스텀 메서드를 포함하고 있습니다.</p>
 *
 * <ul>
 *     <li>findByMateIdAndApplicantId: 특정 게시글에 특정 사용자가 신청했는지 확인</li>
 * </ul>
 *
 * @author zelly
 * @since 2025-04-07
 */
public interface MateApplicationRepository extends JpaRepository<MateApplication, Long> {

    /**
     * mateId와 userId를 이용하여 신청 내역을 조회합니다.
     *
     * @param mateId
     * @param userId
     * @return
     */
    Optional<MateApplication> findByMateIdAndApplicantId(Long mateId, Long userId);

    List<MateApplication> findByMateId(Long mateId);

    boolean existsByMateIdAndApplicantId(Long mateId, Long applicantId);

    @Query("""
                SELECT a FROM MateApplication a
                JOIN FETCH a.mate m
                JOIN FETCH a.applicant u
                WHERE m.writer.id = :writerId
                ORDER BY a.createdAt DESC
            """)
    List<MateApplication> findAllByWriterId(@Param("writerId") Long writerId);

    @Query("""
    SELECT a FROM MateApplication a
    JOIN FETCH a.mate m
    JOIN FETCH m.writer w
    WHERE a.applicant.id = :userId
    ORDER BY a.createdAt DESC
""")
    List<MateApplication> findAllByApplicantId(@Param("userId") Long userId);
}
