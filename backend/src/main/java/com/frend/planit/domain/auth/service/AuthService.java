package com.frend.planit.domain.auth.service;

import com.frend.planit.domain.auth.client.OAuthClient;
import com.frend.planit.domain.auth.client.OAuthClientFactory;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.response.OAuthTokenResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.SocialLoginResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.mapper.UserMapper;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.global.security.JwtTokenProvider;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
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
    public SocialLoginResponse authentiate(SocialLoginRequest request) {
        OAuthClient client = oauthClientFactory.getClient(request.getSocialType());

        OAuthTokenResponse tokenResponse = client.getAccessToken(request.getCode());
        OAuthUserInfoResponse userInfo = client.getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findBySocialIdAndSocialType(userInfo.getSocialId(),
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

        boolean needAdditionalInfo = (user.getStatus() == UserStatus.UNREGISTERED);

        return new SocialLoginResponse(accessToken, refreshToken, needAdditionalInfo,
                user.getEmail());
    }

    /**
     * Refresh Token을 검증하여 새로운 Access Token을 발급.
     */
    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String savedToken = refreshTokenRedisService.get(userId);

        if (!refreshToken.equals(savedToken)) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());

        // refreshToken 만료 임박 시 새로 발급
        String newRefreshToken = refreshToken;
        if (isExpiringSoon(refreshToken)) {
            newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenRedisService.save(user.getId(), newRefreshToken,
                    jwtTokenProvider.getRefreshTokenExpirationMs());
        }

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    // refreshToken 만료 값이 3일 이내 체크
    private boolean isExpiringSoon(String refreshToken) {
        Date expiration = jwtTokenProvider.getExpiration(refreshToken);
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        return remainingTime < Duration.ofDays(3).toMillis();
    }

    /**
     * 로그아웃,, JWT에서 userId 추출, Redis에서 해당 user refreshToken 삭제
     */
    public void logout(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        refreshTokenRedisService.delete(userId);
    }

    public String generateRedirectUri(SocialType socialType) {
        OAuthClient client = oauthClientFactory.getClient(socialType);

        String clientId = client.getClientId();
        String redirectUri = client.getRedirectUri();

        String scope = "openid email profile";
        String responseType = "code";
        String accessType = "offline";
        String prompt = "consent";

        if (socialType == SocialType.KAKAO) {
            scope = "profile_nickname profile_image";
        } else if (socialType == SocialType.NAVER) {
            scope = "name";
        }

        return UriComponentsBuilder
                .fromUriString(getOauthBaseUrl(socialType))
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", responseType)
                .queryParam("scope", scope)
                .queryParam("access_type", accessType)
                .queryParam("prompt", prompt)
                .build()
                .toUriString();
    }

    // 프론트에 baseurl 넘기기 위한 메서드
    private String getOauthBaseUrl(SocialType type) {
        return switch (type) {
            case GOOGLE -> "https://accounts.google.com/o/oauth2/v2/auth";
            case KAKAO -> "https://kauth.kakao.com/oauth/authorize";
            case NAVER -> "https://nid.naver.com/oauth2.0/authorize";
        };
    }
}
