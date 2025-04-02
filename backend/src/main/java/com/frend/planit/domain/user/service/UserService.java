package com.frend.planit.domain.user.service;

import com.frend.planit.domain.user.client.OAuthClient;
import com.frend.planit.domain.user.client.OAuthClientFactory;
import com.frend.planit.domain.user.dto.request.SocialLoginRequest;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.user.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.user.dto.response.SocialLoginResponse;
import com.frend.planit.domain.user.dto.response.UserMeResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final OAuthClientFactory oauthClientFactory;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 소셜 로그인 또는 회원가입
     */
    public SocialLoginResponse loginOrRegister(SocialLoginRequest request) {
        OAuthClient client = oauthClientFactory.getClient(request.getSocialType());

        GoogleTokenResponse tokenResponse = client.getAccessToken(request.getCode());
        GoogleUserInfoResponse userInfo = client.getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findBySocialIdAndSocialType(userInfo.getSub(),
                        client.getSocialType())
                .orElseGet(() -> {
                    // 신규 유저: UNREGISTERED 상태로 저장
                    User newUser = User.builder()
                            .socialId(userInfo.getSub())
                            .socialType(client.getSocialType())
                            .email(userInfo.getEmail())
                            .profileImage(userInfo.getPicture())
                            .nickname(userInfo.getName())
                            .role(Role.USER)
                            .status(UserStatus.UNREGISTERED)
                            .loginType(LoginType.SOCIAL)
                            .build();
                    return userRepository.save(newUser);
                });

        // 마지막 로그인 시간 갱신
        user.updateLastLoginAt(LocalDateTime.now());

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return new SocialLoginResponse(accessToken, refreshToken, user.getStatus());
    }

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
