package dev.bnacar.springx.security.config;

import dev.bnacar.springx.security.aspect.SecuredAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Auto-configuration for Spring X security features.
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    private final SecurityProperties securityProperties;

    /**
     * Constructs a new SecurityAutoConfiguration with the specified properties.
     *
     * @param securityProperties the security properties
     */
    public SecurityAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * Creates the SecuredAspect bean.
     *
     * @return the SecuredAspect bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.security.role-based-security.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(SecuredAspect.class)
    public SecuredAspect securedAspect() {
        return new SecuredAspect();
    }

    /**
     * Creates the CorsFilter bean.
     *
     * @return the CorsFilter bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.security.cors.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(CorsFilter.class)
    public CorsFilter corsFilter() {
        SecurityProperties.CorsProperties corsProperties = securityProperties.getCors();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(java.util.Arrays.asList(corsProperties.getAllowedOrigins()));
        corsConfiguration.setAllowedMethods(java.util.Arrays.asList(corsProperties.getAllowedMethods()));
        corsConfiguration.setAllowedHeaders(java.util.Arrays.asList(corsProperties.getAllowedHeaders()));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
