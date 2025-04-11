package com.frend.planit.domain.mateboard.post.service;

import static com.frend.planit.global.response.ErrorType.MATE_POST_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.NOT_AUTHORIZED;
import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

import com.frend.planit.domain.image.service.ImageService;
import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.domain.mateboard.post.dto.request.MateRequestDto;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.mapper.MateMapper;
import com.frend.planit.domain.mateboard.post.repository.MateQueryRepository;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 메이트 모집 게시글에 대한 비즈니스 로직을 처리하는 Service 클래스입니다.
 * <p>게시글 생성, 단건 조회, 전체 조회, 수정, 삭제 등의 CRUD 기능을 제공합니다.</p>
 * <p>Repository를 통해 데이터베이스에 접근하며, 추후 상태 자동 변경 등 추가 로직도 이 클래스에 구현합니다.</p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-03-30
 */
@Service
@RequiredArgsConstructor
public class MateService {

    private final MateQueryRepository mateQueryRepository;
    private final MateRepository mateRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    /**
     * ID로 게시글을 조회하고, 존재하지 않으면 예외를 던집니다.
     *
     * @param id 조회할 게시글 ID
     * @return 조회된 Mate 엔티티
     * @throws ServiceException 게시글이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public Mate findMateOrThrow(Long id) {
        return mateRepository.findById(id).
                orElseThrow(() -> new ServiceException(MATE_POST_NOT_FOUND));
    }

    /**
     * 메이트 모집 게시글을 생성합니다.
     *
     * @param userId         작성자 ID
     * @param mateRequestDto 클라이언트로부터 받은 게시글 생성 요청 정보
     * @return 생성된 게시글 ID
     */
    public Long createMate(Long userId, MateRequestDto mateRequestDto) {

        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        // 1. 메이트 엔티티 생성
        Mate mate = createMateEntity(writer, mateRequestDto);

        // 2. 생성된 게시글 저장
        mateRepository.save(mate);

        // 3. 이미지 연결(선택)
        if (mateRequestDto.getImageId() != null) {
            imageService.saveImage(HolderType.MATEBOARD, mate.getId(), mateRequestDto.getImageId());
        }

        // 4. 생성된 게시글 ID 반환
        return mate.getId();
    }

    /**
     * 메이트 게시글을 단건 조회합니다.
     *
     * @param id 조회할 게시글 ID
     * @return 조회된 게시글의 응답 DTO
     * @throws RuntimeException 해당 ID의 게시글이 존재하지 않을 경우 예외 발생
     */
    public MateResponseDto getMate(Long id) {
        Mate mate = findMateOrThrow(id);

        // 게시글 이미지 조회
        String imageUrl = imageService.getImage(HolderType.MATEBOARD, mate.getId()).imageUrl();

        // DTO 변환 시 이미지 URL 포함
        return MateMapper.toResponseDto(mate, imageUrl);
    }

    /**
     * 검색 조건(keyword, status, region)에 따라 메이트 게시글을 조회합니다. 페이징 처리를 포함하며, 검색 조건이 없을 경우 전체 조회와 동일합니다.
     *
     * @param keyword  검색 키워드 (제목, 내용, 작성자)
     * @param status   모집 상태 (OPEN, CLOSED)
     * @param region   여행 지역 (서울, 부산 등)
     * @param pageable 페이징 정보
     * @return 필터링된 게시글 응답 DTO 목록
     */
    @Transactional(readOnly = true)
    public PageResponse<MateResponseDto> searchMatePosts(String keyword, String status,
            String region, Pageable pageable) {
        // DB에서 전체 게시글 조회
        Page<MateResponseDto> result = mateQueryRepository.searchMatePosts(keyword, status, region,
                pageable);
        // Entity에서 DTO로 변환하여 반환
        return new PageResponse<>(result);
    }

    /**
     * 메이트 모집 게시글을 수정합니다.
     *
     * @param id             수정할 게시글 ID
     * @param mateRequestDto 클라이언트로부터 전달 받은 수정 요청 데이터
     * @param userId         로그인한 사용자 ID (작성자 확인용)
     * @return 수정된 게시글의 응답 DTO
     */
    public MateResponseDto updateMate(Long id, MateRequestDto mateRequestDto, Long userId) {
        // 1. 수정할 게시글 찾기
        Mate updateMate = findMateOrThrow(id);

        // 2. 작성자 확인
        if (!updateMate.getWriter().getId().equals(userId)) {
            throw new ServiceException(NOT_AUTHORIZED);
        }

        // 3. 수정할 게시글 내용 입력
        updateMate.setTitle(mateRequestDto.getTitle());
        updateMate.setContent(mateRequestDto.getContent());
        updateMate.setTravelRegion(mateRequestDto.getTravelRegion());
        updateMate.setTravelStartDate(mateRequestDto.getTravelStartDate());
        updateMate.setTravelEndDate(mateRequestDto.getTravelEndDate());
        updateMate.setMateGender(mateRequestDto.getMateGender());
        updateMate.setRecruitCount(mateRequestDto.getRecruitCount());

        // 4. 수정한 게시글 저장
        mateRepository.save(updateMate);

        // 5. 이미지 수정 시 반영(선택)
        if (mateRequestDto.getImageId() != null) {
            if (mateRequestDto.getImageId() == 0) { // 0이면 삭제
                imageService.deleteImage(HolderType.MATEBOARD, updateMate.getId());
            } else { // 0이 아니면 변경
                imageService.updateImage(HolderType.MATEBOARD, updateMate.getId(),
                        mateRequestDto.getImageId());
            }
        } // null이면 유지

        String imageUrl = imageService.getImage(HolderType.MATEBOARD, updateMate.getId())
                .imageUrl();

        // 6. 수정한 게시글 전달
        updateMate = findMateOrThrow(id);
        return MateMapper.toResponseDto(updateMate, imageUrl);
    }

    /**
     * 메이트 모집 게시글을 삭제합니다.
     *
     * @param id     삭제할 게시글 ID
     * @param userId 로그인한 사용자 ID (작성자 확인용)
     * @return 삭제된 게시글 응답 DTO
     */
    public MateResponseDto deleteMate(Long id, Long userId) {
        // 1. 게시글 조회 -> 없을 시 예외
        Mate deleteMate = findMateOrThrow(id);

        // 2. 작성자 확인
        if (!deleteMate.getWriter().getId().equals(userId)) {
            throw new ServiceException(NOT_AUTHORIZED);
        }

        // 3. 게시글 백업
        String imageUrl = imageService.getImage(HolderType.MATEBOARD, deleteMate.getId())
                .imageUrl();
        MateResponseDto mateResponseDto = MateMapper.toResponseDto(deleteMate, imageUrl);

        // 4. 이미지 삭제
        imageService.deleteImage(HolderType.MATEBOARD, deleteMate.getId());

        // 5. 게시글 삭제
        mateRepository.delete(deleteMate);

        // 6. 삭제된 게시글 정보 리턴
        return mateResponseDto;
    }

    /**
     * MateRequestDto와 userId를 기반으로 Mate 엔티티를 생성합니다.
     *
     * @param writer         사용자 ID
     * @param mateRequestDto 게시글 요청 DTO
     * @return 생성된 Mate 엔티티
     */
    private Mate createMateEntity(User writer, MateRequestDto mateRequestDto) {
        Mate mate = new Mate();
        mate.setWriter(writer);
        mate.setTitle(mateRequestDto.getTitle());
        mate.setContent(mateRequestDto.getContent());
        mate.setTravelRegion(mateRequestDto.getTravelRegion());
        mate.setTravelStartDate(mateRequestDto.getTravelStartDate());
        mate.setTravelEndDate(mateRequestDto.getTravelEndDate());
        mate.setMateGender(mateRequestDto.getMateGender());
        mate.setRecruitCount(mateRequestDto.getRecruitCount());
        return mate;
    }
}