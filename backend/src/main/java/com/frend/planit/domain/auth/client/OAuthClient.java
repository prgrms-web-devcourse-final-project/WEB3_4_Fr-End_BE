package com.frend.planit.domain.auth.client;

import com.frend.planit.domain.auth.dto.response.GoogleTokenResponse;
import com.frend.planit.domain.auth.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.user.enums.SocialType;

/**
 * 소셜 로그인 확장을 위한 공통 인터페이스 Google / Kakao / Naver OAuth 클라이언트가 이 인터페이스를 구현하게 됨
 */
public interface OAuthClient {

    /**
     * 인가 코드(code)를 통해 소셜 access token 받아오기
     */
    GoogleTokenResponse getAccessToken(String code);

    /**
     * access token 으로 사용자 정보 조회
     */
    GoogleUserInfoResponse getUserInfo(String accessToken);

    /**
     * 어떤 소셜 타입인지 (GOOGLE / KAKAO / NAVER)
     */
    SocialType getSocialType();
}