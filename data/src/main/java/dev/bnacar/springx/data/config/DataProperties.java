package dev.bnacar.springx.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring X data features.
 */
@ConfigurationProperties(prefix = "springx.data")
public class DataProperties {

    private final RepositoryCachingProperties repositoryCaching = new RepositoryCachingProperties();
    private final QueryBuilderProperties queryBuilder = new QueryBuilderProperties();

    /**
     * Gets the repository caching properties.
     *
     * @return the repository caching properties
     */
    public RepositoryCachingProperties getRepositoryCaching() {
        return repositoryCaching;
    }

    /**
     * Gets the query builder properties.
     *
     * @return the query builder properties
     */
    public QueryBuilderProperties getQueryBuilder() {
        return queryBuilder;
    }

    /**
     * Configuration properties for repository caching.
     */
    public static class RepositoryCachingProperties {
        /**
         * Whether repository caching is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether repository caching is enabled.
         *
         * @return whether repository caching is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether repository caching is enabled.
         *
         * @param enabled whether repository caching is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Configuration properties for the query builder.
     */
    public static class QueryBuilderProperties {
        /**
         * Whether the query builder is enabled.
         */
        private boolean enabled = true;

        /**
         * Gets whether the query builder is enabled.
         *
         * @return whether the query builder is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether the query builder is enabled.
         *
         * @param enabled whether the query builder is enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
