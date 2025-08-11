package com.nightfire.tonkotsu.core.domain.model

/**
 * Represents a streaming service with its name and URL.
 *
 * @property name The name of the streaming service (e.g., "Netflix", "Hulu").
 * @property url The URL of the streaming service.
 */
data class NavigableLink(
    val name: String,
    val url: String
)
