package com.frend.planit.domain.auth.service;

import com.frend.planit.domain.auth.client.OAuthClient;
import com.frend.planit.domain.auth.client.OAuthClientFactory;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.auth.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.mapper.UserMapper;
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

    //Refresh Token을 검증하여 새로운 Access Token을 발급.
    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ServiceException(ErrorType.UNAUTHORIZED);
        }
        //refreshToken 값에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String savedToken = refreshTokenRedisService.get(userId);

        //Redis에 저장된 Refresh Token과 일치하는지 확인
        if (!refreshToken.equals(savedToken)) {
            throw new ServiceException(ErrorType.UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));
        //유효할 경우, 새로운 Access Token을 발급하여 반환
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        return new TokenRefreshResponse(newAccessToken);
    }
}
