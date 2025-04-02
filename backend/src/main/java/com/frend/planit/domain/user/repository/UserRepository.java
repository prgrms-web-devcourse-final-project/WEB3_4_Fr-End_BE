package com.frend.planit.domain.user.repository;

import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    boolean existsByNickname(String nickname);
}
