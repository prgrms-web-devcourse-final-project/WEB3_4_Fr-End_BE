package com.frend.planit.domain.user.dto.request;

import com.frend.planit.domain.user.enums.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFirstInfoRequest {

    private String email;

    private String nickname;

    private String phone;

    private LocalDate birthDate;

    private Gender gender;

    private Boolean mailingType;
}