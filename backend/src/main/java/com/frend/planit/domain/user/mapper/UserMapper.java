package com.frend.planit.domain.user.mapper;

import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;

public class UserMapper {

    public static User toEntity(OAuthUserInfoResponse userInfo, SocialType socialType) {
        return User.builder()
                .socialId(userInfo.getSocialId())
                .socialType(socialType)
                .email(userInfo.getEmail())
                .profileImageUrl(userInfo.getProfileImage())
                .nickname(userInfo.getName())
                .role(Role.USER)
                .status(UserStatus.UNREGISTERED)
                .loginType(LoginType.SOCIAL)
                .build();
    }
}