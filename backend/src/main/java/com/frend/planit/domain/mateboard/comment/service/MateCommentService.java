package com.frend.planit.domain.mateboard.comment.service;

import static com.frend.planit.global.response.ErrorType.MATE_COMMENT_NOT_FOUND;
import static com.frend.planit.global.response.ErrorType.NOT_AUTHORIZED;
import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

import com.frend.planit.domain.mateboard.comment.dto.request.MateCommentRequestDto;
import com.frend.planit.domain.mateboard.comment.dto.response.CommentLikeInfo;
import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.comment.mapper.MateCommentMapper;
import com.frend.planit.domain.mateboard.comment.repository.MateCommentRepository;
import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.service.MateService;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.PageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 메이트 모집 게시글 댓글에 대한 비즈니스 로직은 처리하는 Service 클래스입니다.
 * <p> 댓글 생성, 댓글 조회, 수정, 삭제 등의 CRUD 기능을 제공합니다. </p>
 *
 * @author zelly
 * @version 1.0
 * @since 2025-04-02
 */
@Service
@RequiredArgsConstructor
public class MateCommentService {

    private final UserRepository userRepository;
    private final MateCommentRepository mateCommentRepository;
    private final MateService mateService;

    /**
     * ID로 댓글을 조회하고, 존재하지 않으면 예외를 던집니다.
     *
     * @param id 조회할 댓글 ID
     * @return 조회된 mateComment 엔티티
     * @throws ServiceException 댓글이 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public MateComment findMateCommentOrThrow(Long id) {
        return mateCommentRepository.findById(id).orElseThrow(()
                -> new ServiceException(MATE_COMMENT_NOT_FOUND));
    }

    /**
     * 메이트 모집 게시글에 댓글을 작성합니다.
     *
     * @param userId                작성자 ID
     * @param mateCommentRequestDto 클라이언트로부터 받은 댓글 생성 요청 정보
     * @return 생성된 댓글 ID
     */
    public Long createComment(Long userId, Long mateId,
            MateCommentRequestDto mateCommentRequestDto) {
        // 1. 유저 및 게시글 조회 후 코멘트 엔티티 생성
        MateComment mateComment = createCommentEntity(mateId, userId,
                mateCommentRequestDto.getContent());
        // 2. 생성된 엔티티 저장
        MateComment saved = mateCommentRepository.save(mateComment);
        // 3. 생성된 댓글 ID 반환
        return saved.getId();
    }

    /**
     * 메이트 게시글 댓글을 단건 조회합니다.
     * <p> 이용자가 단건 조회를 할 수는 없지만, 내부적으로 수정과 삭제에 사용되는 메서드입니다. </p>
     *
     * @param id 사용자 ID
     * @return 조회된 댓글의 응답 DTO
     * @throws RuntimeException 해당 ID의 댓글이 존재하지 않을 경우 예외 발생
     */
    public MateCommentResponseDto getComment(Long id) {
        MateComment mateComment = findMateCommentOrThrow(id);
        return MateCommentMapper.toResponseDto(mateComment);
    }

    /**
     * 특정 게시글에 대한 댓글을 전체 조회합니다.
     *
     * @param mateId   조회할 댓글의 게시글 ID
     * @param pageable 페이징 정보 (페이지 번호, 크기, 정렬)
     * @return 페이징 처라된 댓글 응답 DTO 목록
     */
    public PageResponse<MateCommentResponseDto> findAllByMateIdWithPaging(Long mateId,
            Pageable pageable) {
        // 1. 해당 메이트 게시글이 존재하는지 확인 (예외 처리)
        Mate mate = mateService.findMateOrThrow(mateId);
        // 2. 해당 게시글에 달린 댓글을 페이징하여 조회
        Page<MateComment> comments = mateCommentRepository.findAllByMate(mate, pageable);
        // 3. Entity -> DTO 변환 (commentLike 포함)
        Page<MateCommentResponseDto> dtoPage = comments.map(comment -> {
            List<CommentLikeInfo> commentLikes = comment.getCommentLikes().stream()
                    .map(like -> new CommentLikeInfo(
                            like.getUser().getId(),
                            like.getMateComment().getId()
                    )).toList();

            int likeCount = commentLikes.size();
            return MateCommentMapper.toResponseDto(comment, likeCount, commentLikes);
        });

        // 4. PageResponse로 감싸서 반환
        return new PageResponse<>(dtoPage);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param id             수정할 댓글의 ID
     * @param mateRequestDto 클라이언트로부터 전달 받은 수정 요청 데이터
     * @return 수정된 댓글의 응답 DTO
     * @throws RuntimeException 해당 ID의 댓글이 존재하지 않을 경우 예외 발생
     */
    public MateCommentResponseDto updateComment(Long id, MateCommentRequestDto mateRequestDto,
            Long userId) {

        // 작성자 본인인지 확인
        // 1. 수정할 Comment 찾기
        MateComment updateComment = findMateCommentOrThrow(id);
        validateCommentAuthor(updateComment, userId);
        // 2. 수정할 값 입력
        updateComment.setContent(mateRequestDto.getContent());
        // 3. 수정할 Comment 저장
        mateCommentRepository.save(updateComment);
        // 4. 수정한 댓글 id 전달
        return MateCommentMapper.toResponseDto(updateComment);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param id 삭제할 댓글의 ID
     * @return 삭제한 게시글 응답 DTO
     * @throws ServiceException 댓글이 존재하지 않을 경우 예외 발생
     */
    public MateCommentResponseDto deleteComment(Long id, Long userId) {
        // 1. 삭제할 Comment 찾기
        MateComment deleteComment = findMateCommentOrThrow(id);
        // 2. 권한 검증
        validateCommentAuthor(deleteComment, userId);
        // 3. Comment 삭제
        mateCommentRepository.delete(deleteComment);
        // 4. 삭제된 댓글 정보 리턴
        return MateCommentMapper.toResponseDto(deleteComment);
    }

    /**
     * mateCommentRequestDto, userId, mateId를 기반으로 MateComment 엔티티를 생성합니다.
     *
     * @param userId  사용자 ID
     * @param mateId  메이트 모집 게시글 ID
     * @param content 댓글 요청 DTO
     * @return 생성된 MateComment 엔티티
     */
    private MateComment createCommentEntity(Long mateId, Long userId,
            String content) {
        // 1. Comment 작성할 User와 게시글이 있는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        Mate mate = mateService.findMateOrThrow(mateId);
        // 2. Comment 엔티티 생성
        MateComment mateComment = new MateComment();
        mateComment.setUser(user);
        mateComment.setMate(mate);
        mateComment.setContent(content);
        return mateComment;
    }

    private void validateCommentAuthor(MateComment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new ServiceException(NOT_AUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    public List<MateCommentResponseDto> getUserMateComments(Long userId) {
        List<MateComment> comments = mateCommentRepository.findByUserId(userId);

        return comments.stream()
                .map(MateCommentMapper::toResponseDto)
                .toList();
    }
}
