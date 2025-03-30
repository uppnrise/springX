package dev.bnacar.springx.testing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a test method for automatic JSON serialization/deserialization testing.
 * When applied to a test method, the method will be executed with various JSON serialization/deserialization scenarios.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTest {

    /**
     * The class to test JSON serialization/deserialization for.
     *
     * @return the class to test
     */
    Class<?> value();

    /**
     * Whether to test null values.
     *
     * @return true if null values should be tested
     */
    boolean testNullValues() default true;

    /**
     * Whether to test empty collections.
     *
     * @return true if empty collections should be tested
     */
    boolean testEmptyCollections() default true;

    /**
     * Whether to test field name case sensitivity.
     *
     * @return true if field name case sensitivity should be tested
     */
    boolean testCaseSensitivity() default true;
}
