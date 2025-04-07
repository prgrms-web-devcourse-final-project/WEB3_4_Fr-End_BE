package com.frend.planit.domain.user.dto.request;

import com.frend.planit.domain.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFirstInfoRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;

    private LocalDate birthDate;

    private Gender gender;
}