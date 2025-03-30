package dev.bnacar.springx.observability;

import dev.bnacar.springx.observability.config.ObservabilityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Observability functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all observability components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ObservabilityAutoConfiguration.class)
public @interface EnableSpringXObservability {
}
