package dev.bnacar.springx.web.config;

import dev.bnacar.springx.web.controller.ApiVersionRequestMappingHandlerMapping;
import dev.bnacar.springx.web.exception.GlobalExceptionHandler;
import dev.bnacar.springx.web.filter.RequestIdFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Auto-configuration for Spring X web features.
 */
@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    private final WebProperties webProperties;

    /**
     * Constructs a new WebAutoConfiguration with the specified properties.
     *
     * @param webProperties the web properties
     */
    public WebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    /**
     * Creates the ApiVersionRequestMappingHandlerMapping bean.
     *
     * @return the ApiVersionRequestMappingHandlerMapping bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.web.api-versioning.enabled", havingValue = "true", matchIfMissing = true)
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }

    /**
     * Creates the GlobalExceptionHandler bean.
     *
     * @return the GlobalExceptionHandler bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.web.global-exception-handler.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * Creates the RequestIdFilter bean.
     *
     * @return the RequestIdFilter bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.web.request-id-filter.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(RequestIdFilter.class)
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }
}
