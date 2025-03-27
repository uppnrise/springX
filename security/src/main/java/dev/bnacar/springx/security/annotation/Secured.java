package dev.bnacar.springx.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to secure a controller method with role-based access control.
 * When applied to a controller method, the method will be secured with the specified roles.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

    /**
     * Roles allowed to access the method.
     * @return the allowed roles
     */
    String[] value() default {};

    /**
     * Whether all roles are required (AND logic) or any role is sufficient (OR logic).
     * @return true if all roles are required, false if any role is sufficient
     */
    boolean allRolesRequired() default false;
}
