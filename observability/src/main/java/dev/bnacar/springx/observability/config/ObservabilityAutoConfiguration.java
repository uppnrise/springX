package dev.bnacar.springx.observability.config;

import dev.bnacar.springx.observability.aspect.MetricAspect;
import dev.bnacar.springx.observability.health.MemoryHealthIndicator;
import dev.bnacar.springx.observability.tracing.TraceInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Auto-configuration for Spring X observability features.
 */
@Configuration
@EnableConfigurationProperties(ObservabilityProperties.class)
public class ObservabilityAutoConfiguration implements WebMvcConfigurer {

    private final ObservabilityProperties observabilityProperties;

    /**
     * Constructs a new ObservabilityAutoConfiguration with the specified properties.
     *
     * @param observabilityProperties the observability properties
     */
    public ObservabilityAutoConfiguration(ObservabilityProperties observabilityProperties) {
        this.observabilityProperties = observabilityProperties;
    }

    /**
     * Creates the MetricAspect bean.
     *
     * @param meterRegistry the meter registry
     * @return the MetricAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.observability.metrics.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnBean(MeterRegistry.class)
    @ConditionalOnMissingBean(MetricAspect.class)
    public MetricAspect metricAspect(MeterRegistry meterRegistry) {
        return new MetricAspect(meterRegistry);
    }

    /**
     * Creates the MemoryHealthIndicator bean.
     *
     * @return the MemoryHealthIndicator bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.observability.health.memory.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "memoryHealthIndicator")
    public HealthIndicator memoryHealthIndicator() {
        ObservabilityProperties.HealthProperties.MemoryHealthProperties memoryProps =
                observabilityProperties.getHealth().getMemory();
        return new MemoryHealthIndicator(
                memoryProps.getWarningThreshold(),
                memoryProps.getCriticalThreshold());
    }

    /**
     * Creates the TraceInterceptor bean.
     *
     * @return the TraceInterceptor bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.observability.tracing.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(TraceInterceptor.class)
    public TraceInterceptor traceInterceptor() {
        return new TraceInterceptor();
    }

    /**
     * Adds the TraceInterceptor to the interceptor registry.
     *
     * @param registry the interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (observabilityProperties.getTracing().isEnabled()) {
            registry.addInterceptor(traceInterceptor());
        }
    }
}
