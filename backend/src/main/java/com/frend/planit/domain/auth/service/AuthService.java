package com.frend.planit.domain.auth.service;

import com.frend.planit.domain.auth.client.OAuthClient;
import com.frend.planit.domain.auth.client.OAuthClientFactory;
import com.frend.planit.domain.auth.dto.request.LocalLoginRequest;
import com.frend.planit.domain.auth.dto.request.SocialLoginRequest;
import com.frend.planit.domain.auth.dto.response.AuthResponse;
import com.frend.planit.domain.auth.dto.response.OAuthTokenResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.auth.dto.response.TokenRefreshResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인
     */
    public AuthResponse authentiate(SocialLoginRequest request) {
        OAuthClient client = oauthClientFactory.getClient(request.getSocialType());

        OAuthTokenResponse tokenResponse = client.getAccessToken(request.getCode());
        OAuthUserInfoResponse userInfo = client.getUserInfo(tokenResponse.getAccessToken());

        User user = userRepository.findBySocialIdAndSocialType(userInfo.getSocialId(),
                        client.getSocialType())
                .orElseGet(() ->
                        userRepository.save(
                                UserMapper.toEntity(userInfo, client.getSocialType())
                        ));
        user.updateLastLoginAt(LocalDateTime.now());

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        refreshTokenRedisService.save(
                user.getId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpirationMs()
        );

        boolean needAdditionalInfo = (user.getStatus() == UserStatus.UNREGISTERED);

        return new AuthResponse(
                accessToken,
                refreshToken,
                needAdditionalInfo,
                null
        );

    }

    /**
     * 로컬 로그인
     */
    public AuthResponse localLogin(LocalLoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new ServiceException(ErrorType.REQUEST_NOT_VALID));

        if (user.getLoginType() != LoginType.LOCAL) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        user.updateLastLoginAt(LocalDateTime.now());

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        refreshTokenRedisService.save(
                user.getId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpirationMs()
        );

        return new AuthResponse(accessToken, refreshToken, false, user.getEmail());
    }

    /**
     * Refresh Token을 검증하여 새로운 Access Token을 발급
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

        String newRefreshToken = refreshToken;
        if (isExpiringSoon(refreshToken)) {
            newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());
            refreshTokenRedisService.save(user.getId(), newRefreshToken,
                    jwtTokenProvider.getRefreshTokenExpirationMs());
        }

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    private boolean isExpiringSoon(String refreshToken) {
        Date expiration = jwtTokenProvider.getExpiration(refreshToken);
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        return remainingTime < Duration.ofDays(3).toMillis();
    }

    /**
     * 로그아웃 처리
     */
    public void logout(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        refreshTokenRedisService.delete(userId);
    }

    /**
     * 프론트에 소셜 로그인 Redirect URL 전달
     */
    public String generateRedirectUri(SocialType socialType) {
        OAuthClient client = oauthClientFactory.getClient(socialType);

        String clientId = client.getClientId();
        String redirectUri = client.getRedirectUri();

        String responseType = "code";
        String accessType = "offline";
        String prompt = "consent";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(getOauthBaseUrl(socialType))
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", responseType)
                .queryParam("access_type", accessType)
                .queryParam("prompt", prompt);

        if (socialType == SocialType.GOOGLE) {
            uriBuilder.queryParam("scope", "openid");
        }

        return uriBuilder.build().toUriString();
    }

    private String getOauthBaseUrl(SocialType type) {
        return switch (type) {
            case GOOGLE -> "https://accounts.google.com/o/oauth2/v2/auth";
            case KAKAO -> "https://kauth.kakao.com/oauth/authorize";
            case NAVER -> "https://nid.naver.com/oauth2.0/authorize";
        };
    }
}
