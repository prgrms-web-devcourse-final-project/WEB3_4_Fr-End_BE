package com.frend.planit.testsecurity;


import com.frend.planit.domain.user.entity.User;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = UserTestFactory.createMockUser(annotation.id(), annotation.username());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, "password", List.of());

        context.setAuthentication(auth);
        return context;
    }
}
