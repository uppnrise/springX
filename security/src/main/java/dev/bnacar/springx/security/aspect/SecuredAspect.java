package dev.bnacar.springx.security.aspect;

import dev.bnacar.springx.security.annotation.Secured;
import dev.bnacar.springx.security.exception.AccessDeniedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aspect that handles the {@link Secured} annotation.
 * Secures methods annotated with {@link Secured} with role-based access control.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "springx.security.role-based-security.enabled", havingValue = "true", matchIfMissing = true)
public class SecuredAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecuredAspect.class);

    /**
     * Intercepts method calls annotated with {@link Secured} and applies role-based security.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @return the result of the method call
     * @throws Throwable if the method call throws an exception or if access is denied
     */
    @Around("@annotation(dev.bnacar.springx.security.annotation.Secured) || @within(dev.bnacar.springx.security.annotation.Secured)")
    public Object secured(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // Get the Secured annotation from the method or class
        Secured methodAnnotation = method.getAnnotation(Secured.class);
        Secured classAnnotation = method.getDeclaringClass().getAnnotation(Secured.class);

        Secured annotation = methodAnnotation != null ? methodAnnotation : classAnnotation;

        if (annotation == null) {
            // This should not happen due to the pointcut expression, but just in case
            return joinPoint.proceed();
        }

        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.debug("Access denied: No authenticated user");
            throw new AccessDeniedException("Access denied: Authentication required");
        }

        // Get the required roles from the annotation
        String[] requiredRoles = annotation.value();

        if (requiredRoles.length == 0) {
            // No specific roles required, just authentication
            return joinPoint.proceed();
        }

        // Get the user's roles
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Check if the user has the required roles
        boolean hasAccess = checkRoles(userRoles, requiredRoles, annotation.allRolesRequired());

        if (!hasAccess) {
            logger.debug("Access denied: User does not have the required roles");
            throw new AccessDeniedException("Access denied: Insufficient privileges");
        }

        // User has the required roles, proceed with the method call
        return joinPoint.proceed();
    }

    /**
     * Checks if the user has the required roles.
     *
     * @param userRoles the user's roles
     * @param requiredRoles the required roles
     * @param allRolesRequired whether all roles are required
     * @return true if the user has the required roles
     */
    private boolean checkRoles(Set<String> userRoles, String[] requiredRoles, boolean allRolesRequired) {
        if (allRolesRequired) {
            // User must have all required roles
            return Arrays.stream(requiredRoles)
                    .allMatch(userRoles::contains);
        } else {
            // User must have at least one of the required roles
            return Arrays.stream(requiredRoles)
                    .anyMatch(userRoles::contains);
        }
    }
}
