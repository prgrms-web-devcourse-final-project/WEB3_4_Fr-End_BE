package com.frend.planit.domain.user.repository;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.SocialType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByNicknameAndIdNot(String nickname, Long id);

    // 메일링 수신 동의 유저 조회
    List<User> findByMailingTypeTrue();
}
