package dev.bnacar.springx.observability.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X observability features.
 */
@ConfigurationProperties(prefix = "springx.observability")
public class ObservabilityProperties {

    private final MetricsProperties metrics = new MetricsProperties();
    private final TracingProperties tracing = new TracingProperties();
    private final HealthProperties health = new HealthProperties();

    /**
     * Gets the metrics properties.
     *
     * @return the metrics properties
     */
    public MetricsProperties getMetrics() {
        return metrics;
    }

    /**
     * Gets the tracing properties.
     *
     * @return the tracing properties
     */
    public TracingProperties getTracing() {
        return tracing;
    }

    /**
     * Gets the health properties.
     *
     * @return the health properties
     */
    public HealthProperties getHealth() {
        return health;
    }

    /**
     * Configuration properties for metrics.
     */
    public static class MetricsProperties {
        /**
         * Whether metrics are enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether metrics are enabled.
         *
         * @return whether metrics are enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether metrics are enabled.
         *
         * @param enabled whether metrics are enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for tracing.
     */
    public static class TracingProperties {
        /**
         * Whether tracing is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether tracing is enabled.
         *
         * @return whether tracing is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether tracing is enabled.
         *
         * @param enabled whether tracing is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for health indicators.
     */
    public static class HealthProperties {
        /**
         * Whether health indicators are enabled.
         */
        private boolean enabled = true;

        /**
         * Memory health indicator properties.
         */
        private final MemoryHealthProperties memory = new MemoryHealthProperties();

        /**
         * Gets whether health indicators are enabled.
         *
         * @return whether health indicators are enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether health indicators are enabled.
         *
         * @param enabled whether health indicators are enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Gets the memory health indicator properties.
         *
         * @return the memory health indicator properties
         */
        public MemoryHealthProperties getMemory() {
            return memory;
        }

        /**
         * Configuration properties for the memory health indicator.
         */
        public static class MemoryHealthProperties {
            /**
             * Whether the memory health indicator is enabled.
             */
            private boolean enabled = true;

            /**
             * The warning threshold as a percentage (0.0-1.0).
             */
            private double warningThreshold = 0.7;

            /**
             * The critical threshold as a percentage (0.0-1.0).
             */
            private double criticalThreshold = 0.9;

            /**
             * Gets whether the memory health indicator is enabled.
             *
             * @return whether the memory health indicator is enabled
             */
            public boolean isEnabled() {
                return enabled;
            }

            /**
             * Sets whether the memory health indicator is enabled.
             *
             * @param enabled whether the memory health indicator is enabled
             */
            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            /**
             * Gets the warning threshold.
             *
             * @return the warning threshold
             */
            public double getWarningThreshold() {
                return warningThreshold;
            }

            /**
             * Sets the warning threshold.
             *
             * @param warningThreshold the warning threshold
             */
            public void setWarningThreshold(double warningThreshold) {
                this.warningThreshold = warningThreshold;
            }

            /**
             * Gets the critical threshold.
             *
             * @return the critical threshold
             */
            public double getCriticalThreshold() {
                return criticalThreshold;
            }

            /**
             * Sets the critical threshold.
             *
             * @param criticalThreshold the critical threshold
             */
            public void setCriticalThreshold(double criticalThreshold) {
                this.criticalThreshold = criticalThreshold;
            }
        }
    }
}
