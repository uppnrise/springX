package dev.bnacar.springx.data;

import dev.bnacar.springx.data.config.DataAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables Spring X Data functionality in a Spring Boot application.
 * Add this annotation to a configuration class to import all data components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DataAutoConfiguration.class)
public @interface EnableSpringXData {
}
