package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;

    private LocalDate birthDate;

    private String bio;

    private boolean mailingType;
}
