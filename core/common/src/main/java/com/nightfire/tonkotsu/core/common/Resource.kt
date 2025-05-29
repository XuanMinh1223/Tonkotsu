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
sealed class Resource<out T>(val data: T? = null, val message: String? = null) {

    class Success<out T>(data: T) : Resource<T>(data)

    class Error<out T>(message: String, data: T? = null) : Resource<T>(data, message)

    object Loading : Resource<Nothing>()
}