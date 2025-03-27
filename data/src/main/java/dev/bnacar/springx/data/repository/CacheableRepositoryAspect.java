package dev.bnacar.springx.data.repository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Aspect that handles the {@link Cacheable} annotation.
 * Caches results of repository methods annotated with {@link Cacheable}.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "springx.data.repository-caching.enabled", havingValue = "true", matchIfMissing = true)
public class CacheableRepositoryAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheableRepositoryAspect.class);

    // Map of cache name to cache
    private final Map<String, Cache> caches = new ConcurrentHashMap<>();

    // Executor service for cache eviction
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Intercepts method calls annotated with {@link Cacheable} and applies caching.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @return the cached result or the result of the method call
     * @throws Throwable if the method call throws an exception
     */
    @Around("@annotation(dev.bnacar.springx.data.repository.Cacheable)")
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Cacheable annotation = method.getAnnotation(Cacheable.class);

        String cacheName = getCacheName(annotation, method);
        Cache cache = getOrCreateCache(cacheName, annotation.ttlSeconds());

        // Generate cache key based on method parameters
        String cacheKey = generateCacheKey(method, joinPoint.getArgs());

        // Check if result is in cache
        if (cache.containsKey(cacheKey)) {
            CacheEntry entry = cache.get(cacheKey);
            logger.debug("Cache hit for {}#{} with key {}",
                    method.getDeclaringClass().getSimpleName(), method.getName(), cacheKey);
            return entry.getValue();
        }

        // Execute method and cache result
        Object result = joinPoint.proceed();

        // Only cache if result is not null or cacheNull is true
        if (result != null || annotation.cacheNull()) {
            logger.debug("Caching result for {}#{} with key {}",
                    method.getDeclaringClass().getSimpleName(), method.getName(), cacheKey);
            cache.put(cacheKey, new CacheEntry(result));
        }

        return result;
    }

    /**
     * Gets the cache name from the annotation or generates a default one.
     *
     * @param annotation the Cacheable annotation
     * @param method     the method being cached
     * @return the cache name
     */
    private String getCacheName(Cacheable annotation, Method method) {
        if (annotation.cacheName() != null && !annotation.cacheName().isEmpty()) {
            return annotation.cacheName();
        }

        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    /**
     * Gets or creates a cache with the specified name and TTL.
     *
     * @param cacheName  the cache name
     * @param ttlSeconds the TTL in seconds
     * @return the cache
     */
    private Cache getOrCreateCache(String cacheName, long ttlSeconds) {
        return caches.computeIfAbsent(cacheName, name -> {
            logger.debug("Creating cache: {}", name);
            return new Cache(ttlSeconds);
        });
    }

    /**
     * Generates a cache key based on the method and its arguments.
     *
     * @param method the method
     * @param args   the method arguments
     * @return the cache key
     */
    private String generateCacheKey(Method method, Object[] args) {
        return method.getName() + ":" + Arrays.deepHashCode(args);
    }

    /**
     * Simple cache implementation.
     */
    private class Cache {
        private final Map<String, CacheEntry> entries = new ConcurrentHashMap<>();
        private final long ttlMillis;

        /**
         * Constructs a new Cache with the specified TTL.
         *
         * @param ttlSeconds the TTL in seconds
         */
        public Cache(long ttlSeconds) {
            this.ttlMillis = ttlSeconds * 1000;

            // Schedule eviction of expired entries
            scheduler.scheduleAtFixedRate(this::evictExpiredEntries,
                    ttlSeconds, ttlSeconds, TimeUnit.SECONDS);
        }

        /**
         * Checks if the cache contains the specified key.
         *
         * @param key the key
         * @return true if the cache contains the key
         */
        public boolean containsKey(String key) {
            CacheEntry entry = entries.get(key);
            if (entry == null) {
                return false;
            }

            if (entry.isExpired()) {
                entries.remove(key);
                return false;
            }

            return true;
        }

        /**
         * Gets the cache entry for the specified key.
         *
         * @param key the key
         * @return the cache entry
         */
        public CacheEntry get(String key) {
            return entries.get(key);
        }

        /**
         * Puts a cache entry for the specified key.
         *
         * @param key   the key
         * @param entry the cache entry
         */
        public void put(String key, CacheEntry entry) {
            entries.put(key, entry);
        }

        /**
         * Evicts expired entries from the cache.
         */
        private void evictExpiredEntries() {
            entries.entrySet().removeIf(entry -> entry.getValue().isExpired());
        }
    }

    /**
     * Cache entry with expiration.
     */
    private class CacheEntry {
        private final Object value;
        private final long expirationTime;

        /**
         * Constructs a new CacheEntry with the specified value.
         *
         * @param value the value
         */
        public CacheEntry(Object value) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() +
                    caches.values().iterator().next().ttlMillis;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public Object getValue() {
            return value;
        }

        /**
         * Checks if the entry is expired.
         *
         * @return true if the entry is expired
         */
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
