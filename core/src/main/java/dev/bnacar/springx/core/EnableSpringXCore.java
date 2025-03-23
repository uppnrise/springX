package dev.bnacar.springx.core;

import org.springframework.context.annotation.Import;
import dev.bnacar.springx.core.config.SpringXAutoConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Core functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all core components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SpringXAutoConfiguration.class)
public @interface EnableSpringXCore {
}
