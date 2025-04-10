package com.frend.planit.global.init.auth;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.Gender;
import com.frend.planit.domain.user.enums.LoginType;
import com.frend.planit.domain.user.enums.Role;
import com.frend.planit.domain.user.enums.UserStatus;
import com.frend.planit.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalUserBaseInitData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 5) {
            return;
        }

        List<User> users = List.of(
                createUser("ricky01", "ricky01@email.com", "Ricky", "01012345678"),
                createUser("mina02", "mina02@email.com", "Mina", "01098765432"),
                createUser("junho03", "junho03@email.com", "Junho", "01011112222"),
                createUser("sohee04", "sohee04@email.com", "Sohee", "01033334444"),
                createUser("taeyang05", "taeyang05@email.com", "Taeyang", "01055556666")
        );

        userRepository.saveAll(users);
    }

    private User createUser(String loginId, String email, String nickname, String phone) {
        return User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode("1234"))
                .email(email)
                .nickname(nickname)
                .phone(phone)
                .bio("자기소개입니다.")
                .gender(Gender.UNSPECIFIED)
                .birthDate(LocalDate.of(1995, 1, 1))
                .socialType(null)
                .loginType(LoginType.LOCAL)
                .status(UserStatus.REGISTERED)
                .mailingType(false)
                .role(Role.USER)
                .build();
    }
}
