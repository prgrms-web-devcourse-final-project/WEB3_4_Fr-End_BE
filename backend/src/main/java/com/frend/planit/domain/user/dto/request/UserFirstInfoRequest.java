package com.frend.planit.domain.user.dto.request;

import com.frend.planit.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserFirstInfoRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;

    private LocalDate birthDate;

    private Gender gender;
}
