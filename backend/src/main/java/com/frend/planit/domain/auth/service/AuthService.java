package com.frend.planit.domain.auth.service;

import com.frend.planit.domain.auth.client.OAuthClient;
import com.frend.planit.domain.auth.client.OAuthClientFactory;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.auth.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.mapper.UserMapper;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final OAuthClientFactory oauthClientFactory;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;

    /**
     * 소셜 로그인 또는 회원가입
     */

    public SocialLoginResponse loginOrRegister(SocialLoginRequest request) {
        OAuthClient client = oauthClientFactory.getClient(request.getSocialType());

        GoogleTokenResponse tokenResponse = client.getAccessToken(request.getCode());
        GoogleUserInfoResponse userInfo = client.getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findBySocialIdAndSocialType(userInfo.getSub(),
                        client.getSocialType())
                .orElseGet(() ->
                        userRepository.save(
                                UserMapper.toEntity(userInfo, client.getSocialType())
                        ));
        // 마지막 로그인 시간 갱신
        user.updateLastLoginAt(LocalDateTime.now());

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        refreshTokenRedisService.save(
                user.getId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpirationMs()
        );

        return new SocialLoginResponse(accessToken, refreshToken, user.getStatus());
    }
}
