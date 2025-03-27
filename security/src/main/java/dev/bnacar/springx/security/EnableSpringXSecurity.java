package dev.bnacar.springx.security;

import dev.bnacar.springx.security.config.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Security functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all security components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SecurityAutoConfiguration.class)
public @interface EnableSpringXSecurity {
}
