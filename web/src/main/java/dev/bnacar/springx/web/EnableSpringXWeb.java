package dev.bnacar.springx.web;

import org.springframework.context.annotation.Import;
import dev.bnacar.springx.web.config.WebAutoConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Web functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all web components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WebAutoConfiguration.class)
public @interface EnableSpringXWeb {
}
