package com.nightfire.tonkotsu.core.domain.model

import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * Represents detailed information about a specific anime.
 * This class is typically used to display comprehensive information on an anime detail screen.
 *
 * @property id The unique identifier of the anime.
 * @property title The primary title of the anime.
 * @property imageUrl The URL for the large image of the anime, suitable for a detail screen.
 * @property synopsis A summary of the anime's plot.
 * @property score The average score given to the anime, if available.
 * @property scoredBy The number of users who have scored the anime, if available.
 * @property rank The ranking of the anime based on score, if available.
 * @property popularity The ranking of the anime based on how many users have it in their list, if available.
 * @property members The total number of users who have the anime in their list, if available.
 * @property favorites The number of users who have favorited the anime, if available.
 * @property episodes The total number of episodes in the anime, if available.
 * @property status The airing status of the anime (e.g., "Finished Airing", "Currently Airing").
 * @property duration The duration of each episode (e.g., "23 min per ep").
 * @property rating The age rating of the anime (e.g., "PG-13 - Teens 13 or older").
 * @property source The original source material of the anime (e.g., "Manga", "Light novel").
 * @property season The season in which the anime premiered (e.g., "Spring", "Summer").
 * @property year The year in which the anime premiered.
 * @property genres A list of genre names associated with the anime (e.g., "Action", "Adventure").
 * @property studios A list of names of the animation studios involved in the anime's production.
 * @property producers A list of names of the producers involved in the anime's production.
 * @property licensors A list of names of the licensors of the anime.
 * @property background Additional background information or context about the anime, if available.
 * @property trailerYoutubeUrl The YouTube video ID for the anime's trailer, if available.
 * @property relations A map grouping related anime entries by their relation type (e.g., "Sequel", "Prequel").
 */
data class AnimeDetail(
    val id: Int,
    val title: String,
    val alternativeTitle: String?,
    val japaneseTitle: String?,
    val imageUrl: String, // Large image URL for the detail screen
    val synopsis: String,
    val type: String?,
    val score: Double?,
    val scoredBy: Int?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val episodes: Int?,
    val status: String?,
    val duration: String?,
    val rating: String?,
    val source: String?,
    val season: String?,
    val year: Int?,
    val broadcast: String?,
    val premiereDate: OffsetDateTime?,
    val endDate: OffsetDateTime?,
    val genres: List<String>,
    val themes: List<String>,
    val categories: List<String>,
    val studios: List<String>,
    val producers: List<String>,
    val licensors: List<String>,
    val background: String?,
    val trailerYoutubeUrl: String?,
    val trailerYoutubeId: String?,
    val relations: Map<String, List<RelationEntry>>,
    val streamingLinks: List<NavigableLink>,
    val openingThemes: List<String>,
    val endingThemes: List<String>,
    val externalLinks: List<NavigableLink>
)
