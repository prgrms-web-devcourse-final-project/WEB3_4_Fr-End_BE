package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateProfileRequest {

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;

    private String profileImageUrl;

    private LocalDate birthDate;

    private String bio;

    private boolean mailingType; // false 일 경우 프로필 조회에 신청, ture 일 경우 프로필 조회에 안뜸
}
