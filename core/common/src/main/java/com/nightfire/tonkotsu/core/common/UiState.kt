package com.nightfire.tonkotsu.core.common

/**
 * A generalized UI state class to represent the common states of a UI screen or component.
 * This can be used across different ViewModels to standardize UI state management.
 *
 * @param T The type of data that the UI component displays.
 * @param isLoading True if data is currently being fetched or processed.
 * @param data The actual data to be displayed, if available.
 * @param errorMessage An error message string, if an error occurred.
 */
data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val errorMessage: String? = null,
    val isRetrying: Boolean = false
) {
    // Companion object for convenience functions to create specific states
    companion object {
        fun <T> loading(data: T? = null) = UiState(isLoading = true, data = data)
        fun <T> success(data: T) = UiState(isLoading = false, data = data)
        fun <T> error(message: String, data: T? = null, isRetrying: Boolean = false) = UiState(isLoading = false, errorMessage = message, data = data)
    }
}