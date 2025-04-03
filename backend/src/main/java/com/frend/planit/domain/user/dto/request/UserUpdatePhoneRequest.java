package com.frend.planit.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdatePhoneRequest {

    @Pattern(regexp = "^\\d{11}$", message = "휴대폰 번호는 숫자 11자리여야 합니다.")
    private String phone;
}
