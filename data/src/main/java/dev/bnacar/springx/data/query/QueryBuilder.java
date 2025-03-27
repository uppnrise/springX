package dev.bnacar.springx.data.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fluent query builder for constructing SQL queries.
 * Provides a type-safe way to build SQL queries programmatically.
 */
public class QueryBuilder {

    private final StringBuilder query = new StringBuilder();
    private final List<Object> parameters = new ArrayList<>();
    private boolean whereStarted = false;

    /**
     * Creates a new QueryBuilder with a SELECT clause.
     *
     * @param columns the columns to select
     * @return the QueryBuilder instance
     */
    public static QueryBuilder select(String... columns) {
        QueryBuilder builder = new QueryBuilder();
        builder.query.append("SELECT ");

        if (columns.length == 0) {
            builder.query.append("*");
        } else {
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) {
                    builder.query.append(", ");
                }
                builder.query.append(columns[i]);
            }
        }

        return builder;
    }

    /**
     * Adds a FROM clause to the query.
     *
     * @param table the table name
     * @return the QueryBuilder instance
     */
    public QueryBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    /**
     * Adds a WHERE clause to the query.
     *
     * @param condition the condition
     * @param params the parameters for the condition
     * @return the QueryBuilder instance
     */
    public QueryBuilder where(String condition, Object... params) {
        if (whereStarted) {
            query.append(" AND ").append(condition);
        } else {
            query.append(" WHERE ").append(condition);
            whereStarted = true;
        }

        parameters.addAll(Arrays.asList(params));

        return this;
    }

    /**
     * Adds an AND condition to the WHERE clause.
     *
     * @param condition the condition
     * @param params the parameters for the condition
     * @return the QueryBuilder instance
     */
    public QueryBuilder and(String condition, Object... params) {
        if (!whereStarted) {
            throw new IllegalStateException("Cannot add AND condition without a WHERE clause");
        }

        query.append(" AND ").append(condition);

        parameters.addAll(Arrays.asList(params));

        return this;
    }

    /**
     * Adds an OR condition to the WHERE clause.
     *
     * @param condition the condition
     * @param params the parameters for the condition
     * @return the QueryBuilder instance
     */
    public QueryBuilder or(String condition, Object... params) {
        if (!whereStarted) {
            throw new IllegalStateException("Cannot add OR condition without a WHERE clause");
        }

        query.append(" OR ").append(condition);

        parameters.addAll(Arrays.asList(params));

        return this;
    }

    /**
     * Adds an ORDER BY clause to the query.
     *
     * @param columns the columns to order by
     * @return the QueryBuilder instance
     */
    public QueryBuilder orderBy(String... columns) {
        query.append(" ORDER BY ");

        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                query.append(", ");
            }
            query.append(columns[i]);
        }

        return this;
    }

    /**
     * Adds a LIMIT clause to the query.
     *
     * @param limit the limit
     * @return the QueryBuilder instance
     */
    public QueryBuilder limit(int limit) {
        query.append(" LIMIT ?");
        parameters.add(limit);
        return this;
    }

    /**
     * Adds an OFFSET clause to the query.
     *
     * @param offset the offset
     * @return the QueryBuilder instance
     */
    public QueryBuilder offset(int offset) {
        query.append(" OFFSET ?");
        parameters.add(offset);
        return this;
    }

    /**
     * Gets the SQL query string.
     *
     * @return the SQL query string
     */
    public String getQuery() {
        return query.toString();
    }

    /**
     * Gets the query parameters.
     *
     * @return the query parameters
     */
    public Object[] getParameters() {
        return parameters.toArray();
    }
}
