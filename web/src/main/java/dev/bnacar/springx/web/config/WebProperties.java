package dev.bnacar.springx.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X web features.
 */
@ConfigurationProperties(prefix = "spring-x.web")
public class WebProperties {

    private final ApiVersioningProperties apiVersioning = new ApiVersioningProperties();
    private final GlobalExceptionHandlerProperties globalExceptionHandler = new GlobalExceptionHandlerProperties();
    private final RequestIdFilterProperties requestIdFilter = new RequestIdFilterProperties();

    /**
     * Gets the API versioning properties.
     *
     * @return the API versioning properties
     */
    public ApiVersioningProperties getApiVersioning() {
        return apiVersioning;
    }

    /**
     * Gets the global exception handler properties.
     *
     * @return the global exception handler properties
     */
    public GlobalExceptionHandlerProperties getGlobalExceptionHandler() {
        return globalExceptionHandler;
    }

    /**
     * Gets the request ID filter properties.
     *
     * @return the request ID filter properties
     */
    public RequestIdFilterProperties getRequestIdFilter() {
        return requestIdFilter;
    }

    /**
     * Configuration properties for API versioning.
     */
    public static class ApiVersioningProperties {
        /**
         * Whether API versioning is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether API versioning is enabled.
         *
         * @return whether API versioning is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether API versioning is enabled.
         *
         * @param enabled whether API versioning is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for the global exception handler.
     */
    public static class GlobalExceptionHandlerProperties {
        /**
         * Whether the global exception handler is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the global exception handler is enabled.
         *
         * @return whether the global exception handler is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the global exception handler is enabled.
         *
         * @param enabled whether the global exception handler is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for the request ID filter.
     */
    public static class RequestIdFilterProperties {
        /**
         * Whether the request ID filter is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the request ID filter is enabled.
         *
         * @return whether the request ID filter is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the request ID filter is enabled.
         *
         * @param enabled whether the request ID filter is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
