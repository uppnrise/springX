package dev.bnacar.springx.testing;

import dev.bnacar.springx.testing.config.TestingAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Testing functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all testing components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestingAutoConfiguration.class)
public @interface EnableSpringXTesting {
}
