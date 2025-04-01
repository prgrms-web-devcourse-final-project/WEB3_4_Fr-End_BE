package com.frend.planit.domain.mateboard.post.service;

import static com.frend.planit.global.response.ErrorType.MATE_POST_NOT_FOUND;

import com.frend.planit.domain.mateboard.post.dto.request.MateRequestDto;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.mapper.MateMapper;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
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

    private final MateRepository mateRepository;

    /**
     * 메이트 모집 게시글을 생성합니다.
     *
     * @param userId         작성자 ID
     * @param mateRequestDto 클라이언트로부터 받은 게시글 생성 요청 정보
     * @return 생성된 게시글 ID
     */
    public Long createMate(Long userId, MateRequestDto mateRequestDto) {
        // 1. 메이트 엔티티 생성
        Mate mate = new Mate();
        mate.setUserId(userId);
        mate.setTitle(mateRequestDto.getTitle());
        mate.setContent(mateRequestDto.getContent());
        mate.setTravelRegion(mateRequestDto.getTravelRegion());
        mate.setTravelStartDate(mateRequestDto.getTravelStartDate());
        mate.setTravelEndDate(mateRequestDto.getTravelEndDate());
        mate.setMateGender(mateRequestDto.getMateGender());
        // 2. 생성된 게시글 저장
        mateRepository.save(mate);
        // 3. 생성된 게시글 ID 반환
        return mate.getId();
    }

    /**
     * 전체 메이트 모집 게시글을 페이징 처리하여 조회합니다.
     *
     * @param pageable 페이징 정보 (페이지 번호, 크기, 정렬)
     * @return 페이징 처리된 게시글 응답 DTO 목록
     */
    public PageResponse<MateResponseDto> findAllWithPaging(Pageable pageable) {
        // DB에서 전체 게시글 조회
        Page<Mate> mates = mateRepository.findAll(pageable);
        Page<MateResponseDto> dtoPage = mates.map(MateMapper::toResponseDto);
        // Entity에서 DTO로 변환하여 반환
        return new PageResponse<>(dtoPage);
    }

    /**
     * 메이트 게시글을 단건 조회합니다.
     *
     * @param id 조회할 게시글 ID
     * @return 조회된 게시글의 응답 DTO
     * @throws RuntimeException 헤당 Id의 게시글이 존재하지 않을 경우 예외 발생
     */
    public MateResponseDto getMate(Long id) {
        Mate mate = findMateOrThrow(id);
        return MateMapper.toResponseDto(mate);
    }

    /**
     * 메이트 모집 게시글을 수정합니다.
     *
     * @param id             수정할 게시글 ID
     * @param mateRequestDto 클라이언트로부터 전달 받은 수정 요청 데이터
     * @return 수정된 게시글의 응답 DTO
     * @throws RuntimeException 해당 id의 게시글이 존재하지 않을 경우 예외 발생
     */
    public MateResponseDto updateMate(Long id, MateRequestDto mateRequestDto) {
        // 1. 수정할 게시글 찾기
        Mate updatemate = findMateOrThrow(id);
        // 2. 수정할 게시글 내용 입력
        updatemate.setTitle(mateRequestDto.getTitle());
        updatemate.setContent(mateRequestDto.getContent());
        updatemate.setTravelRegion(mateRequestDto.getTravelRegion());
        updatemate.setTravelStartDate(mateRequestDto.getTravelStartDate());
        updatemate.setTravelEndDate(mateRequestDto.getTravelEndDate());
        updatemate.setMateGender(mateRequestDto.getMateGender());
        // 3. 수정한 게시글 저장
        mateRepository.save(updatemate);
        // 4. 수정한 게시글 id 전달
        return MateMapper.toResponseDto(updatemate);
    }

    /**
     * 메이트 모집 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글 ID
     * @return 삭제할 게시글의 응답 DTO
     * @throws RuntimeException 해당 Id의 게시글이 존재하지 않을 경우 예외 발생
     */
    public MateResponseDto deleteMate(Long id) {
        // 1. 게시글 조회 -> 없을 시 예외
        Mate deleteMate = findMateOrThrow(id);
        // 2. 게시글 삭제
        mateRepository.delete(deleteMate);
        // 3. 삭제된 게시글 정보 리턴
        return MateMapper.toResponseDto(deleteMate);
    }
    
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
}