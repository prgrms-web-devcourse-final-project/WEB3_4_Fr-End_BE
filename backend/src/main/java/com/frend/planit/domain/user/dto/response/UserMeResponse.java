package com.frend.planit.domain.user.dto.response;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserMeResponse {

    private Long id;
    private String nickname;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate birthDate;
    private String profileImage;
    private String bio;
    private UserStatus status;
    private LocalDateTime createdAt;
    private SocialType socialType;
    private boolean mailingType;
    private Role role;

    public static UserMeResponse from(User user) {
        return UserMeResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail()) // 출력만 하고, 수정은 프론트에서 제한
                .phone(user.getPhone())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .profileImage(user.getProfileImageUrl())
                .bio(user.getBio())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .socialType(user.getSocialType())
                .mailingType(user.isMailingType())
                .role(user.getRole())
                .build();
    }
}
