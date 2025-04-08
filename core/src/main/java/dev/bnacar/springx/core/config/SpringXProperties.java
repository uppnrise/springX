package dev.bnacar.springx.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X AOP features.
 */
@ConfigurationProperties(prefix = "spring-x.aop")
public class SpringXProperties {

    private final LogExecutionTimeProperties logExecutionTime = new LogExecutionTimeProperties();
    private final RetryProperties retry = new RetryProperties();
    private final ThrottleProperties throttle = new ThrottleProperties();

    /**
     * Gets the log execution time properties.
     *
     * @return the log execution time properties
     */
    public LogExecutionTimeProperties getLogExecutionTime() {
        return logExecutionTime;
    }

    /**
     * Gets the retry properties.
     *
     * @return the retry properties
     */
    public RetryProperties getRetry() {
        return retry;
    }

    /**
     * Gets the throttle properties.
     *
     * @return the throttle properties
     */
    public ThrottleProperties getThrottle() {
        return throttle;
    }

    /**
     * Configuration properties for the @LogExecutionTime aspect.
     */
    public static class LogExecutionTimeProperties {
        /**
         * Whether the @LogExecutionTime aspect is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the @LogExecutionTime aspect is enabled.
         *
         * @return whether the aspect is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the @LogExecutionTime aspect is enabled.
         *
         * @param enabled whether the aspect is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for the @Retry aspect.
     */
    public static class RetryProperties {
        /**
         * Whether the @Retry aspect is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the @Retry aspect is enabled.
         *
         * @return whether the aspect is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the @Retry aspect is enabled.
         *
         * @param enabled whether the aspect is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for the @Throttle aspect.
     */
    public static class ThrottleProperties {
        /**
         * Whether the @Throttle aspect is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the @Throttle aspect is enabled.
         *
         * @return whether the aspect is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the @Throttle aspect is enabled.
         *
         * @param enabled whether the aspect is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
