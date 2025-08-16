package com.nightfire.tonkotsu.core.domain.model

data class AnimeSearchQuery(
    val query: String? = null,
    val type: String? = null,
    val score: Double? = null,
    val minScore: Double? = null,
    val maxScore: Double? = null,
    val status: String? = null,
    val rating: String? = null,
    val sfw: Boolean? = null,
    val genres: String? = null,
    val genresExclude: String? = null,
    val orderBy: String? = "popularity",
    val sort: String? = "desc",
    val letter: String? = null,
    val producers: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val page: Int? = null,
    val limit: Int? = null
)

fun AnimeSearchQuery.toQueryMap(): Map<String, Any?> {
    return mapOf(
        "q" to query,
        "type" to type,
        "score" to score,
        "min_score" to minScore,
        "max_score" to maxScore,
        "status" to status,
        "rating" to rating,
        "sfw" to sfw,
        "genres" to genres,
        "genres_exclude" to genresExclude,
        "order_by" to orderBy,
        "sort" to sort,
        "letter" to letter,
        "producers" to producers,
        "start_date" to startDate,
        "end_date" to endDate,
        "page" to page,
        "limit" to limit
    ).filterValues { it != null }
}
