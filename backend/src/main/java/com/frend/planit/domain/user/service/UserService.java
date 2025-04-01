package com.frend.planit.domain.user.service;

import com.frend.planit.domain.user.client.GoogleOauthClient;
import com.frend.planit.domain.user.dto.request.UserFirstInfoRequest;
import com.frend.planit.domain.user.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.user.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.user.dto.response.LoginResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final GoogleOauthClient googleOauthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 구글 인가 코드를 받아 로그인 또는 회원가입 처리
     */
    public LoginResponse loginOrRegister(String code) {
        // 1. access token 요청
        GoogleTokenResponse tokenResponse = googleOauthClient.getAccessToken(code);

        // 2. 사용자 정보 요청
        GoogleUserInfoResponse userInfo = googleOauthClient.getUserInfo(
                tokenResponse.getAccessToken());

        // 3. 소셜 식별자 기반 유저 찾기
        User user = userRepository.findBySocialIdAndSocialType(userInfo.getSub(), SocialType.GOOGLE)
                .orElseGet(() -> {
                    // 신규 유저: UNREGISTERED 상태로 생성
                    User newUser = User.builder()
                            .socialId(userInfo.getSub())
                            .socialType(SocialType.GOOGLE)
                            .email(userInfo.getEmail())
                            .profileImage(userInfo.getPicture())
                            .nickname(userInfo.getName())
                            .role(Role.USER)
                            .status(UserStatus.UNREGISTERED)
                            .loginType(LoginType.SOCIAL)
                            .build();
                    return userRepository.save(newUser);
                });

        // 4. 마지막 로그인 시간 업데이트
        user.updateLastLoginAt(LocalDateTime.now());

        // 5. JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 6. 응답 반환
        return new LoginResponse(accessToken, refreshToken, user.getStatus());
    }

    /**
     * 최초 로그인 시 사용자 추가 정보 입력
     */
    public void updateFirstInfo(Long userId, UserFirstInfoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (user.getStatus() != UserStatus.UNREGISTERED) {
            throw new IllegalStateException("이미 최초 정보를 입력한 사용자입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
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
}
