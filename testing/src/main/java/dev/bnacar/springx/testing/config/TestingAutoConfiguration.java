package dev.bnacar.springx.testing.config;

import dev.bnacar.springx.testing.runner.JsonTestExtension;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Spring X testing features.
 */
@Configuration
@EnableConfigurationProperties(TestingProperties.class)
public class TestingAutoConfiguration {

    private final TestingProperties testingProperties;

    /**
     * Constructs a new TestingAutoConfiguration with the specified properties.
     *
     * @param testingProperties the testing properties
     */
    public TestingAutoConfiguration(TestingProperties testingProperties) {
        this.testingProperties = testingProperties;
    }

    /**
     * Creates the JsonTestExtension bean.
     *
     * @return the JsonTestExtension bean
     */
    @Bean
    @ConditionalOnProperty(name = "springx.testing.json-test.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(JsonTestExtension.class)
    public JsonTestExtension jsonTestExtension() {
        return new JsonTestExtension();
    }
}
