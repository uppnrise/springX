package dev.bnacar.springx.core.config;

import dev.bnacar.springx.core.aop.LogExecutionTimeAspect;
import dev.bnacar.springx.core.aop.RetryAspect;
import dev.bnacar.springx.core.aop.ThrottleAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Spring X core features.
 */
@Configuration
@EnableConfigurationProperties(SpringXProperties.class)

public class SpringXAutoConfiguration {

    /**
     * Creates the LogExecutionTimeAspect bean.
     *
     * @return the LogExecutionTimeAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.aop.log-execution-time.enabled", havingValue = "true", matchIfMissing = true)
    public LogExecutionTimeAspect logExecutionTimeAspect() {
        return new LogExecutionTimeAspect();
    }

    /**
     * Creates the RetryAspect bean.
     *
     * @return the RetryAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.aop.retry.enabled", havingValue = "true", matchIfMissing = true)
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    /**
     * Creates the ThrottleAspect bean.
     *
     * @return the ThrottleAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.aop.throttle.enabled", havingValue = "true", matchIfMissing = true)
    public ThrottleAspect throttleAspect() {
        return new ThrottleAspect();
    }
}
