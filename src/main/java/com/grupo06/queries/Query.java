package com.grupo06.queries;

/**
 * Represents a generic query operation.
 *
 * @param <T> the type of result returned by the query
 */
public interface Query<T> {
    /**
     * Executes the query using the provided data source.
     *
     * @param data the data context for the query
     * @return the result of the query
     */
    T execute(QueryData data);
}
