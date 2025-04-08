package dev.bnacar.springx.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X testing features.
 */
@ConfigurationProperties(prefix = "spring-x.testing")
public class TestingProperties {

    private final JsonTestProperties jsonTest = new JsonTestProperties();
    private final MockProperties mock = new MockProperties();

    /**
     * Gets the JSON test properties.
     *
     * @return the JSON test properties
     */
    public JsonTestProperties getJsonTest() {
        return jsonTest;
    }

    /**
     * Gets the mock properties.
     *
     * @return the mock properties
     */
    public MockProperties getMock() {
        return mock;
    }

    /**
     * Configuration properties for JSON tests.
     */
    public static class JsonTestProperties {
        /**
         * Whether JSON tests are enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether JSON tests are enabled.
         *
         * @return whether JSON tests are enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether JSON tests are enabled.
         *
         * @param enabled whether JSON tests are enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for mocks.
     */
    public static class MockProperties {
        /**
         * Whether mocks are enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether mocks are enabled.
         *
         * @return whether mocks are enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether mocks are enabled.
         *
         * @param enabled whether mocks are enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
