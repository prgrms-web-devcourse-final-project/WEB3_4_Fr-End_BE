package com.frend.planit.domain.user.dto.response;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.Gender;
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

    public static UserMeResponse from(User user) {
        return UserMeResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
