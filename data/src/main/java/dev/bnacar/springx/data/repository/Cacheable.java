package dev.bnacar.springx.data.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a repository method for query caching.
 * When applied to a repository method, the results will be cached
 * based on the method parameters.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * Cache name to use for storing the results.
     * If not specified, a default cache name will be used based on the repository class and method name.
     * @return the cache name
     */
    String cacheName() default "";

    /**
     * Time-to-live for the cached results in seconds.
     * @return the TTL in seconds
     */
    long ttlSeconds() default 300;

    /**
     * Whether to cache null results.
     * @return true if null results should be cached
     */
    boolean cacheNull() default true;
}
