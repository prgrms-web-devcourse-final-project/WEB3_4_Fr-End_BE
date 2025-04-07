package com.frend.planit.domain.mateboard.application.service;

import static com.frend.planit.global.response.ErrorType.ALREADY_APPLIED;
import static com.frend.planit.global.response.ErrorType.INVALID_CANCEL_STATUS;
import static com.frend.planit.global.response.ErrorType.MATE_ALREADY_CLOSED;
import static com.frend.planit.global.response.ErrorType.MATE_ALREADY_FULL;
import static com.frend.planit.global.response.ErrorType.MATE_APPLICATION_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.NOT_AUTHORIZED;
import static com.frend.planit.global.response.ErrorType.SELF_APPLICATION_NOT_ALLOWED;
import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

import com.frend.planit.domain.mateboard.application.entity.MateApplication;
import com.frend.planit.domain.mateboard.application.entity.MateApplicationStatus;
import com.frend.planit.domain.mateboard.application.repository.MateApplicationRepository;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.RecruitmentStatus;
import com.frend.planit.domain.mateboard.post.service.MateService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 메이트 모집 신청과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * <p>다음과 같은 기능을 제공합니다:</p>
 * <ul>
 *     <li>사용자가 메이트 모집 게시글에 신청</li>
 *     <li>신청 취소</li>
 *     <li>작성자가 신청 수락</li>
 *     <li>작성자가 신청 거절</li>
 * </ul>
 *
 * <p>사용자 및 게시글 정보는 각각의 Repository를 통해 조회하며,</p>
 * <p>권한 검증, 상태 검증, 인원수 제한 체크 등의 핵심 로직을 포함하고 있습니다.</p>
 *
 * @author zelly
 * @since 2025-04-07
 */
@Service
@RequiredArgsConstructor
public class MateApplicationService {

    private final MateApplicationRepository mateApplicationRepository;
    private final UserRepository userRepository;
    private final MateService mateService;

    /**
     * 메이트 모집 게시글에 신청합니다.
     *
     * @param userId 신청자 ID
     * @param mateId 신청한 게시글 ID
     * @throws ServiceException 조건 불충족 시 예외 발생
     */
    @Transactional
    public void applyToMate(Long userId, Long mateId) {

        // 1. 사용자 조회
        User applicant = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        // 2. 게시글 조회
        Mate mate = mateService.findMateOrThrow(mateId);

        // 3. 자기 글에 신청하는 경우 예외
        if (mate.getWriter().getId().equals(userId)) {
            throw new ServiceException(SELF_APPLICATION_NOT_ALLOWED);
        }

        // 4. 모집 상태 확인
        if (!mate.getRecruitmentStatus().equals(RecruitmentStatus.OPEN)) {
            throw new ServiceException(MATE_ALREADY_CLOSED);
        }

        // 5. 모집 인원이 다 찼는지 확인
        if (mate.getRecruitCount() <= mate.getApplications().stream()
                .filter(a -> a.getStatus() == MateApplicationStatus.ACCEPTED)
                .count()) {
            throw new ServiceException(MATE_ALREADY_FULL);
        }
        // 6. 이미 신청했는지 확인
        boolean alreadyApplied = mate.getApplications().stream().
                anyMatch(app -> app.getApplicant().getId().equals(userId));

        if (alreadyApplied) {
            throw new ServiceException(ALREADY_APPLIED);
        }

        // 7. 신청 객체 생성 및 저장
        MateApplication application = MateApplication.builder()
                .mate(mate)
                .applicant(applicant)
                .status(MateApplicationStatus.PENDING)
                .build();

        mateApplicationRepository.save(application);
    }

    /**
     * 메이트 모집 신청을 취소합니다.
     *
     * @param mateId 신청한 게시글 ID
     * @param userId 취소하는 사용자 ID
     * @throws ServiceException 조건 불충족 시 예외 발생
     */
    @Transactional
    public void cancelApplication(Long mateId, Long userId) {

        // 1. 신청 내역이 있는지 조회(mateId와 applicantId)
        MateApplication mateApplication = mateApplicationRepository
                .findByMateIdAndApplicantId(mateId, userId)
                .orElseThrow(() -> new ServiceException(MATE_APPLICATION_NOT_FOUND));

        // 2. 로그인한 사용자가 신청자 본인인지 검증
        if (!mateApplication.getApplicant().getId().equals(userId)) {
            throw new ServiceException(NOT_AUTHORIZED);
        }

        // 3. 신청 상태가 대기 중일 때만 취소 가능
        validateRecruitmentNotFull(mateApplication.getMate());
        // 4. 신청 내역 삭제
        mateApplicationRepository.delete(mateApplication);
    }

    /**
     * 메이트 모집 신청을 수락합니다.
     *
     * @param mateId      모집 게시글 ID
     * @param applicantId 신청자 ID
     * @param writerId    게시글 작성자 ID (현재 로그인한 사용자)
     * @throws ServiceException 조건 불충족 시 예외 발생
     */
    @Transactional
    public void acceptApplication(Long mateId, Long applicantId, Long writerId) {
        // 1. 신청 내역 조회
        MateApplication mateApplication = mateApplicationRepository.findByMateIdAndApplicantId(
                        mateId, applicantId)
                .orElseThrow(() -> new ServiceException(MATE_APPLICATION_NOT_FOUND));

        // 2. 작성자 검증
        Mate mate = mateApplication.getMate();
        validateAuthor(mate, writerId);

        // 3. 모집 상태 확인
        if (mate.getRecruitmentStatus() != RecruitmentStatus.OPEN) {
            throw new ServiceException(MATE_ALREADY_CLOSED);
        }

        // 4. 신청 상태 확인 + 인원 초과 여부 확인
        validateRecruitmentNotFull(mateApplication.getMate());

        // 6. 수락 처리
        mateApplication.accept();

        // 7. 인원이 다 찼으면 모집 종료
        mate.closeRecruitmentIfFull();
    }

    /**
     * 메이트 모집 신청을 거절합니다.
     *
     * @param mateId      모집 게시글 ID
     * @param applicantId 신청자 ID
     * @param writerId    게시글 작성자 ID (현재 로그인한 사용자)
     * @throws ServiceException 조건 불충족 시 예외 발생
     */
    @Transactional
    public void rejectApplication(Long mateId, Long applicantId, Long writerId) {
        // 1. 신청 내역 조회
        MateApplication mateApplication = mateApplicationRepository.findByMateIdAndApplicantId(
                        mateId, applicantId)
                .orElseThrow(() -> new ServiceException(MATE_APPLICATION_NOT_FOUND));

        // 2. 작성자 검증
        Mate mate = mateApplication.getMate();
        validateAuthor(mate, writerId);

        // 3. 신청 상태 확인
        validatePendingStatus(mateApplication);

        // 4. 상태 REJECTED로 변경
        mateApplication.reject();
    }

    // 작성자 검증
    private void validateAuthor(Mate mate, Long writerId) {
        if (!mate.getWriter().getId().equals(writerId)) {
            throw new ServiceException(NOT_AUTHORIZED);
        }
    }

    // 신청 상태 검증
    private void validatePendingStatus(MateApplication mateApplication) {
        if (mateApplication.getStatus() != MateApplicationStatus.PENDING) {
            throw new ServiceException(INVALID_CANCEL_STATUS);
        }
    }

    // 수락 인원 체크 로직 메서드화
    private void validateRecruitmentNotFull(Mate mate) {
        long acceptedCount = mate.getApplications().stream()
                .filter(app -> app.getStatus() == MateApplicationStatus.ACCEPTED)
                .count();
        if (acceptedCount >= mate.getRecruitCount()) {
            throw new ServiceException(MATE_ALREADY_FULL);
        }
    }
}