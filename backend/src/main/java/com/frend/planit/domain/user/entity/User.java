package com.frend.planit.domain.user.entity;

import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.SocialType;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"social_id", "social_type"})
        }
)
public class User extends BaseTime {

    @Column(name = "social_id", length = 255)
    private String socialId;

    @Column(unique = true)
    private String loginId;

    @Column(length = 255)
    private String password;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 50, unique = true)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @Column(length = 255)
    private String bio; // 자기소개

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Gender gender = Gender.UNSPECIFIED;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "mailing_type")
    @Builder.Default
    private boolean mailingType = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.UNREGISTERED;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    public void updateFirstInfo(String email, String nickname, String phone, LocalDate birthDate,
            Gender gender, Boolean mailingType) {
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mailingType = mailingType;
        this.status = UserStatus.REGISTERED;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    public void updateMailingType(boolean mailingType) {
        this.mailingType = mailingType;
    }


    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;

    }

    public void updateEmail(
            @NotBlank(message = "이메일은 필수입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") String email) {
        this.email = email;

    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateLastLoginAt(LocalDateTime now) {
        this.lastLoginAt = now;
    }

}