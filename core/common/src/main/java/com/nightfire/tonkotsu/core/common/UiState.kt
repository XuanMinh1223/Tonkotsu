package com.nightfire.tonkotsu.core.common

/**
 * Sealed class representing various UI states for data loading.
 * This provides a more robust and type-safe way to handle different states
 * (Loading, Success, Error) in your UI.
 */
sealed class UiState<out T> {
    /**
     * Represents a loading state.
     * @param data Optional data that might still be present from a previous successful load
     * while new data is being fetched (e.g., for pull-to-refresh).
     */
    data class Loading<out T>(val data: T? = null) : UiState<T>()

    /**
     * Represents a successful state with data.
     * @param data The successfully loaded data.
     */
    data class Success<out T>(val data: T) : UiState<T>()

    /**
     * Represents an error state.
     * @param message The error message to display to the user.
     * @param data Optional data that might still be present, even in an error state.
     */
    data class Error<out T>(val message: String, val data: T? = null) : UiState<T>()
}