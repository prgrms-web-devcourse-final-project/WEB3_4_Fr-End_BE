package com.frend.planit.domain.user.dto.request;

import com.frend.planit.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFirstInfoRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    private String nickname;

    private String phone;

    private LocalDate birthDate;

    private Gender gender;
}