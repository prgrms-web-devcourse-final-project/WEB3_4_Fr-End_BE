package com.frend.planit.domain.mateboard.comment.service;

import static com.frend.planit.global.response.ErrorType.USER_NOT_FOUND;

import com.frend.planit.domain.mateboard.comment.entity.MateComment;
import com.frend.planit.domain.mateboard.comment.entity.MateCommentLike;
import com.frend.planit.domain.mateboard.comment.repository.MateCommentLikeRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateCommentLikeService {

    private final MateCommentService mateCommentService;
    private final MateCommentLikeRepository mateCommentLikeRepository;
    private final UserRepository userRepository;

    /**
     * 특정 댓글에 좋아요를 누릅니다.
     *
     * @param commentId
     * @param userId
     */
    @Transactional
    public void like(Long commentId, Long userId) {

        // 1. user 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        // 2. 댓글이 존재하는지 확인
        MateComment mateComment = mateCommentService.findMateCommentOrThrow(commentId);

        // 3. 이미 좋아요를 눌렀는지 확인, 이미 눌렀다면 예외
        boolean alreadyLiked = mateCommentLikeRepository.findByUserAndMateComment(user, mateComment)
                .isPresent();

        if (alreadyLiked) {
            throw new ServiceException(ErrorType.ALREADY_LIKED);
        }
        // 4. 좋아요 안 눌렀으면 저장
        MateCommentLike mateCommentLike = new MateCommentLike(mateComment, user);
        mateCommentLikeRepository.save(mateCommentLike);
    }

    /**
     * 특정 댓글에 좋아요를 취소합니다.
     *
     * @param commentId
     * @param userId
     */
    @Transactional
    public void unlike(Long commentId, Long userId) {

        // 1. user 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        // 2. 댓글 존재 확인
        MateComment mateComment = mateCommentService.findMateCommentOrThrow(commentId);

        // 3. 좋아요 기록이 있는지 확인
        MateCommentLike mateCommentLike = mateCommentLikeRepository.findByUserAndMateComment(user,
                        mateComment)
                .orElseThrow(() -> new ServiceException(ErrorType.LIKE_NOT_FOUND));

        // 4. 존재하면 삭제
        mateCommentLikeRepository.delete(mateCommentLike);
    }

    /**
     * 좋아요 수를 조회합니다.
     *
     * @param commentId
     * @return
     */
    @Transactional(readOnly = true)
    public Long getLikeCount(Long commentId) {
        // 댓글 존재 확인
        MateComment mateComment = mateCommentService.findMateCommentOrThrow(commentId);
        return mateCommentLikeRepository.countByMateComment(mateComment);
    }
}