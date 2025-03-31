package dev.bnacar.springx.security.integration;

import dev.bnacar.springx.security.annotation.Secured;
import dev.bnacar.springx.security.aspect.SecuredAspect;
import dev.bnacar.springx.security.exception.AccessDeniedException;
import dev.bnacar.springx.security.util.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SecurityIntegrationTest.TestConfig.class})
public class SecurityIntegrationTest {

    @Autowired
    private TestService testService;

    @BeforeEach
    public void setup() {
        // Clear security context before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testSecuredAspectWithAuthorizedUser() {
        // Set up security context with admin role
        User user = new User("admin", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        // Call secured method that requires ADMIN role
        String result = testService.adminOnlyMethod();

        // Verify the method was executed
        assertEquals("Admin method executed", result);
    }

    @Test
    public void testSecuredAspectWithUnauthorizedUser() {
        // Set up security context with user role
        User user = new User("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        // Call secured method that requires ADMIN role
        assertThrows(AccessDeniedException.class, () -> {
            testService.adminOnlyMethod();
        });
    }

    @Test
    public void testSecuredAspectWithMultipleRoles() {
        // Set up security context with user role
        User user = new User("user", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        // Call secured method that requires USER or ADMIN role
        String result = testService.userOrAdminMethod();

        // Verify the method was executed
        assertEquals("User or admin method executed", result);
    }

    @Test
    public void testSecurityUtils() {
        // Set up security context with user role
        User user = new User("testuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        // Test getCurrentUsername
        assertEquals("testuser", SecurityUtils.getCurrentUsername());

        // Test hasRole
        assertTrue(SecurityUtils.hasRole("ROLE_USER"));
        assertFalse(SecurityUtils.hasRole("ROLE_ADMIN"));

        // Test hasAnyRole
        assertTrue(SecurityUtils.hasAnyRole("ROLE_USER", "ROLE_ADMIN"));
        assertFalse(SecurityUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER"));
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        public TestService testService() {
            return new TestService();
        }

        @Bean
        public SecuredAspect securedAspect() {
            return new SecuredAspect();
        }
    }

    @Service
    static class TestService {

        @Secured("ROLE_ADMIN")
        public String adminOnlyMethod() {
            return "Admin method executed";
        }

        @Secured({"ROLE_USER", "ROLE_ADMIN"})
        public String userOrAdminMethod() {
            return "User or admin method executed";
        }
    }
}
