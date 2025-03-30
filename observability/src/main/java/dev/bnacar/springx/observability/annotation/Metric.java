package dev.bnacar.springx.observability.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method for metric collection.
 * When applied to a method, metrics about the method execution will be collected.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {

    /**
     * Name of the metric.
     * If not specified, the metric name will be derived from the class and method name.
     * @return the metric name
     */
    String value() default "";

    /**
     * Description of the metric.
     * @return the metric description
     */
    String description() default "";

    /**
     * Tags to apply to the metric.
     * @return the metric tags
     */
    String[] tags() default {};

    /**
     * Whether to record the execution time of the method.
     * @return true if execution time should be recorded
     */
    boolean recordExecutionTime() default true;

    /**
     * Whether to record the number of invocations of the method.
     * @return true if invocation count should be recorded
     */
    boolean recordInvocationCount() default true;

    /**
     * Whether to record exceptions thrown by the method.
     * @return true if exceptions should be recorded
     */
    boolean recordExceptions() default true;
}
