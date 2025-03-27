package dev.bnacar.springx.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X security features.
 */
@ConfigurationProperties(prefix = "springx.security")
public class SecurityProperties {

    private final RoleBasedSecurityProperties roleBasedSecurity = new RoleBasedSecurityProperties();
    private final CorsProperties cors = new CorsProperties();
    private final CsrfProperties csrf = new CsrfProperties();

    /**
     * Gets the role-based security properties.
     *
     * @return the role-based security properties
     */
    public RoleBasedSecurityProperties getRoleBasedSecurity() {
        return roleBasedSecurity;
    }

    /**
     * Gets the CORS properties.
     *
     * @return the CORS properties
     */
    public CorsProperties getCors() {
        return cors;
    }

    /**
     * Gets the CSRF properties.
     *
     * @return the CSRF properties
     */
    public CsrfProperties getCsrf() {
        return csrf;
    }

    /**
     * Configuration properties for role-based security.
     */
    public static class RoleBasedSecurityProperties {
        /**
         * Whether role-based security is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether role-based security is enabled.
         *
         * @return whether role-based security is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether role-based security is enabled.
         *
         * @param enabled whether role-based security is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for CORS.
     */
    public static class CorsProperties {
        /**
         * Whether CORS is enabled.
         */
        private boolean enabled = true;

        /**
         * Allowed origins for CORS.
         */
        private String[] allowedOrigins = {"*"};

        /**
         * Allowed methods for CORS.
         */
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

        /**
         * Allowed headers for CORS.
         */
        private String[] allowedHeaders = {"*"};

        /**
         * Gets whether CORS is enabled.
         *
         * @return whether CORS is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether CORS is enabled.
         *
         * @param enabled whether CORS is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Gets the allowed origins for CORS.
         *
         * @return the allowed origins
         */
        public String[] getAllowedOrigins() {
            return allowedOrigins;
        }

        /**
         * Sets the allowed origins for CORS.
         *
         * @param allowedOrigins the allowed origins
         */
        public void setAllowedOrigins(String[] allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        /**
         * Gets the allowed methods for CORS.
         *
         * @return the allowed methods
         */
        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        /**
         * Sets the allowed methods for CORS.
         *
         * @param allowedMethods the allowed methods
         */
        public void setAllowedMethods(String[] allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        /**
         * Gets the allowed headers for CORS.
         *
         * @return the allowed headers
         */
        public String[] getAllowedHeaders() {
            return allowedHeaders;
        }

        /**
         * Sets the allowed headers for CORS.
         *
         * @param allowedHeaders the allowed headers
         */
        public void setAllowedHeaders(String[] allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }
    }

    /**
     * Configuration properties for CSRF.
     */
    public static class CsrfProperties {
        /**
         * Whether CSRF protection is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether CSRF protection is enabled.
         *
         * @return whether CSRF protection is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether CSRF protection is enabled.
         *
         * @param enabled whether CSRF protection is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
