package com.frend.planit.domain.user.mapper;

import com.frend.planit.domain.user.dto.response.GoogleUserInfoResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;

public class UserMapper {

    public static User toEntity(GoogleUserInfoResponse userInfo, SocialType socialType) {
        return User.builder()
                .socialId(userInfo.getSub())
                .socialType(socialType)
                .email(userInfo.getEmail())
                .profileImage(userInfo.getPicture())
                .nickname(userInfo.getName())
                .role(Role.USER)
                .status(UserStatus.UNREGISTERED)
                .loginType(LoginType.SOCIAL)
                .build();
    }
}