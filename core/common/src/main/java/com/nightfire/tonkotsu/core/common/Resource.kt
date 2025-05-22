// core/common/src/main/java/com.nightfire.tonkotsu.core.common/Resource.kt
package com.nightfire.tonkotsu.core.common

/**
 * A generic sealed class to represent the state of an operation (loading, success, error).
 * This pattern is widely used in Android to communicate the status of data fetching
 * or any asynchronous operation from data sources all the way up to the UI.
 *
 * @param T The type of data being loaded or returned.
 * @param data Optional data associated with the success or error state.
 * @param message Optional message associated with the error state.
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    /**
     * Represents a successful operation.
     * @param data The result data of the successful operation.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents a failed operation.
     * @param message The error message describing the failure.
     * @param data Optional data that might still be useful even in an error state (e.g., cached data).
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents an ongoing operation (e.g., data is being fetched).
     * @param data Optional data that might be displayed while loading (e.g., stale data).
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}