package com.frend.planit.domain.user.service;

import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

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
            throw new ServiceException(ErrorType.DUPLICATE_NICKNAME);
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
     * 닉네임 중복 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
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
}
