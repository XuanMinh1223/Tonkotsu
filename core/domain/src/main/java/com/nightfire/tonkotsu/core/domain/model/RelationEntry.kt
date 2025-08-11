package com.nightfire.tonkotsu.core.domain.model

/**
 * Represents a related entry in an anime or manga series.
 *
 * @property id The unique identifier of the related entry.
 * @property name The name of the related entry.
 * @property type The type of the related entry (e.g., "anime", "manga").
 * @property url The URL to the related entry's page.
 */
data class RelationEntry(
    val id: Int,
    val name: String,
    val type: String,
    val url: String
)
