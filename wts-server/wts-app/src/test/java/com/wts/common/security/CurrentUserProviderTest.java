package com.wts.common.security;

import com.wts.common.exception.BizException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrentUserProviderTest {

    private final CurrentUserProvider provider = new CurrentUserProvider();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void requireReturnsAuthenticatedUserFromSecurityContext() {
        LoginUserDetails details = new LoginUserDetails();
        details.setUserId("u-1");
        details.setLoginName("teacher");
        details.setName("Teacher One");
        details.setUserType("1");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(details, null, List.of())
        );

        CurrentUser user = provider.require();

        assertEquals("u-1", user.id());
        assertEquals("teacher", user.loginName());
        assertEquals("Teacher One", user.displayName());
        assertEquals("1", user.userType());
    }

    @Test
    void requireFallsBackToLoginNameForDisplayName() {
        LoginUserDetails details = new LoginUserDetails();
        details.setUserId("u-2");
        details.setLoginName("student");
        details.setUserType("2");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(details, null, List.of())
        );

        CurrentUser user = provider.require();

        assertEquals("student", user.displayName());
    }

    @Test
    void requireRejectsMissingAuthentication() {
        BizException error = assertThrows(BizException.class, provider::require);

        assertEquals(401, error.getCode());
    }
}
