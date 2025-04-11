package com.frend.planit.domain.user.service;

import com.frend.planit.domain.calendar.dto.response.CalendarActivityResponseDto;
import com.frend.planit.domain.calendar.service.CalendarService;
import com.frend.planit.domain.mateboard.comment.dto.response.MateCommentResponseDto;
import com.frend.planit.domain.mateboard.comment.service.MateCommentService;
import com.frend.planit.domain.mateboard.post.dto.response.MateResponseDto;
import com.frend.planit.domain.mateboard.post.service.MateService;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MateService mateService;
    private final MateCommentService mateCommentService;
    private final CalendarService calendarService;

    /**
     * 최초 로그인 시 추가 정보 등록
     */
    public void updateFirstInfo(Long userId, UserFirstInfoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));

        if (user.getStatus() != UserStatus.UNREGISTERED) {
            throw new ServiceException(ErrorType.COMMON_SERVER_ERROR);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        user.updateFirstInfo(
                request.getEmail(),
                request.getNickname(),
                request.getPhone(),
                request.getBirthDate(),
                request.getGender()
        );
    }

    /**
     * 추가 정보 입력 시 닉네임 중복 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    /**
     * 추가 정보 입력 시 이메일 중복 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * 추가 정보 입력 시 이메일 중복 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isPhoneAvailable(String phone) {
        return !userRepository.existsByPhone(phone);
    }

    /**
     * 내 정보 조회
     */
    @Transactional(readOnly = true)
    public UserMeResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));

        return UserMeResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<MateResponseDto> getUserActivity(Long userId) {
        // MateService에서 사용자 활동 내역을 조회
        return mateService.getUserMatePosts(userId);
    }

    @Transactional(readOnly = true)
    public List<MateCommentResponseDto> getUserCommentsActivity(Long userId) {
        return mateCommentService.getUserMateComments(userId);
    }

    @Transactional(readOnly = true)
    public List<CalendarActivityResponseDto> getUserCalendarActivity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.USER_NOT_FOUND));
        return calendarService.getUserCalendarActivity(user);
    }

}
