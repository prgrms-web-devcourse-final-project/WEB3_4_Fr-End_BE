package com.frend.planit.testsecurity;


import com.frend.planit.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class UserTestFactory {

    public static User createMockUser(Long id, String username) {
        User user = User.builder()
                .nickname(username)
                .build();

        // BaseEntity에서 상속받은 id 필드 강제 주입
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}