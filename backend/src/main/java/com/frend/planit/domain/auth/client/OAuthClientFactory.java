package com.frend.planit.domain.auth.client;

import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 소셜 타입에 맞는 OAuthClient 구현체를 주입해주는 팩토리 - 추후 KakaoOAuthClient, NaverOAuthClient 추가 예정
 */
@Component
@RequiredArgsConstructor
public class OAuthClientFactory {

    private final List<OAuthClient> oauthClients;

    public OAuthClient getClient(SocialType socialType) {
        return oauthClients.stream()
                .filter(client -> client.getSocialType() == socialType)
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorType.REQUEST_NOT_VALID));
    }
}