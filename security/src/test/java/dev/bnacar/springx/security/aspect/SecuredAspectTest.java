package dev.bnacar.springx.security.aspect;

import dev.bnacar.springx.security.annotation.Secured;
import dev.bnacar.springx.security.exception.AccessDeniedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecuredAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private SecuredAspect securedAspect;

    @BeforeEach
    void setUp() {
        securedAspect = new SecuredAspect();
    }

    @Test
    void secured_whenNoAnnotation_shouldProceed() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("unsecuredMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("result");

        // Remove security context mocks - they're not needed for this test case
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Act
            Object result = securedAspect.secured(joinPoint);

            // Assert
            assertEquals("result", result);
            verify(joinPoint).proceed();
        }
    }

    @Test
    void secured_whenNotAuthenticated_shouldThrowAccessDeniedException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("securedMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            // Act & Assert
            assertThrows(AccessDeniedException.class, () -> securedAspect.secured(joinPoint));
            verify(joinPoint, never()).proceed();
        }
    }

    @Test
    void secured_whenNoAuthenticationExists_shouldThrowAccessDeniedException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("securedMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // Act & Assert
            assertThrows(AccessDeniedException.class, () -> securedAspect.secured(joinPoint));
            verify(joinPoint, never()).proceed();
        }
    }

    @Test
    void secured_whenNoRolesRequired_shouldProceed() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("authenticatedOnlyMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("result");

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);

            // Act
            Object result = securedAspect.secured(joinPoint);

            // Assert
            assertEquals("result", result);
            verify(joinPoint).proceed();
        }
    }

    @Test
    void secured_whenUserHasRequiredRole_shouldProceed() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("securedMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("result");

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            // Act
            Object result = securedAspect.secured(joinPoint);

            // Assert
            assertEquals("result", result);
            verify(joinPoint).proceed();
        }
    }

    @Test
    void secured_whenUserDoesNotHaveRequiredRole_shouldThrowAccessDeniedException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("securedMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            // Act & Assert
            assertThrows(AccessDeniedException.class, () -> securedAspect.secured(joinPoint));
            verify(joinPoint, never()).proceed();
        }
    }

    @Test
    void secured_whenAllRolesRequired_andUserHasAllRoles_shouldProceed() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("allRolesRequiredMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("result");

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_MANAGER"));

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            // Act
            Object result = securedAspect.secured(joinPoint);

            // Assert
            assertEquals("result", result);
            verify(joinPoint).proceed();
        }
    }

    @Test
    void secured_whenAllRolesRequired_andUserDoesNotHaveAllRoles_shouldThrowAccessDeniedException() throws Throwable {
        // Arrange
        Method method = TestService.class.getMethod("allRolesRequiredMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            // Act & Assert
            assertThrows(AccessDeniedException.class, () -> securedAspect.secured(joinPoint));
            verify(joinPoint, never()).proceed();
        }
    }

    @Test
    void secured_whenClassLevelAnnotation_shouldApplyClassLevelSecurity() throws Throwable {
        // Arrange
        Method method = SecuredClass.class.getMethod("someMethod");
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("result");

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

            // Act
            Object result = securedAspect.secured(joinPoint);

            // Assert
            assertEquals("result", result);
            verify(joinPoint).proceed();
        }
    }

    // Test classes for the tests

    private static class TestService {

        public String unsecuredMethod() {
            return "unsecured";
        }

        @Secured({"ROLE_ADMIN"})
        public String securedMethod() {
            return "secured";
        }

        @Secured
        public String authenticatedOnlyMethod() {
            return "authenticated";
        }

        @Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER"}, allRolesRequired = true)
        public String allRolesRequiredMethod() {
            return "allRolesRequired";
        }
    }

    @Secured({"ROLE_SUPER_ADMIN"})
    private static class SecuredClass {
        public String someMethod() {
            return "classLevelSecurity";
        }
    }
}
