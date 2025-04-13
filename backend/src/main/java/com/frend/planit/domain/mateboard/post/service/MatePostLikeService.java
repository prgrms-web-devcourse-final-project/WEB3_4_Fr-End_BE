package com.frend.planit.domain.mateboard.post.service;

import com.frend.planit.domain.mateboard.post.entity.Mate;
import com.frend.planit.domain.mateboard.post.entity.MatePostLike;
import com.frend.planit.domain.mateboard.post.repository.MatePostLikeRepository;
import com.frend.planit.domain.mateboard.post.repository.MateRepository;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatePostLikeService {

    private final MatePostLikeRepository matePostLikeRepository;
    private final MateRepository mateRepository;
    private final MateService mateService;


    // 좋아요 누르기
    @Transactional
    public void like(Long postId, User user) {
        // 1. 게시글 존재하는지 확인
        Mate mate = mateService.findMateOrThrow(postId);

        boolean alreadyLiked = matePostLikeRepository.findByUserAndMatePost(user, mate)
                .isPresent();

        // 2. 이미 좋아요 눌렀는지 확인
        if (alreadyLiked) {
            throw new ServiceException(ErrorType.ALREADY_LIKED);
        }

        // 3. 좋아요 안 눌렀으면 저장
        MatePostLike like = new MatePostLike(mate, user);
        matePostLikeRepository.save(like);

    }

    // 좋아요 취소
    @Transactional
    public void unLike(Long postId, User user) {
        // 1. 게시글 존재하는지 확인
        Mate mate = mateService.findMateOrThrow(postId);

        // 2. 좋아요 기록 조회
        MatePostLike like = matePostLikeRepository.findByUserAndMatePost(user, mate)
                .orElseThrow(() -> new ServiceException(ErrorType.LIKE_NOT_FOUND));

        // 3. 존재하면 삭제
        matePostLikeRepository.delete(like);

    }

    // 좋아요 수 조회
    @Transactional(readOnly = true)
    public Long getLikeCount(Long matePostId) {
        // 게시글 존재 확인 후 countByMatePost 사용
        Mate mate = mateService.findMateOrThrow(matePostId);

        return matePostLikeRepository.countByMatePost(mate);
    }
}
