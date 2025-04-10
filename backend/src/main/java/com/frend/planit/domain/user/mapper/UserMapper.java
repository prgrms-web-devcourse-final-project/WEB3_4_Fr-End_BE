package com.frend.planit.domain.user.mapper;

import com.frend.planit.domain.auth.dto.request.LocalRegisterRequest;
import com.frend.planit.domain.auth.dto.response.LocalRegisterResponse;
import com.frend.planit.domain.auth.dto.response.OAuthUserInfoResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    // 소셜 로그인용 회원가입 매핑
    public static User toEntity(OAuthUserInfoResponse userInfo, SocialType socialType) {
        return User.builder()
                .socialId(userInfo.getSocialId())
                .socialType(socialType)
                .email(userInfo.getEmail())
                .profileImageUrl(userInfo.getProfileImage())
                .nickname(null)
                .role(Role.USER)
                .status(UserStatus.UNREGISTERED)
                .loginType(LoginType.SOCIAL)
                .build();
    }

    // 로컬 매핑
    public static User toEntity(LocalRegisterRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .profileImageUrl(request.getProfileImageUrl())
                .bio(request.getBio())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .mailingType(request.isMailingType())
                .role(Role.USER)
                .status(UserStatus.REGISTERED)
                .loginType(LoginType.LOCAL)
                .build();
    }

    // 로컬 회원가입 매핑
    public static LocalRegisterResponse toResponse(User user) {
        return new LocalRegisterResponse(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.getEmail()
        );
    }
}