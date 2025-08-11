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
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val message: String, val data: T? = null) : Resource<T>()
}