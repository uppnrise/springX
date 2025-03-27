package dev.bnacar.springx.data.config;

import dev.bnacar.springx.data.repository.CacheableRepositoryAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Spring X data features.
 */
@Configuration
@EnableConfigurationProperties(DataProperties.class)
public class DataAutoConfiguration {

    private final DataProperties dataProperties;

    /**
     * Constructs a new DataAutoConfiguration with the specified properties.
     *
     * @param dataProperties the data properties
     */
    public DataAutoConfiguration(DataProperties dataProperties) {
        this.dataProperties = dataProperties;
    }

    /**
     * Creates the CacheableRepositoryAspect bean.
     *
     * @return the CacheableRepositoryAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.data.repository-caching.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(CacheableRepositoryAspect.class)
    public CacheableRepositoryAspect cacheableRepositoryAspect() {
        return new CacheableRepositoryAspect();
    }
}
