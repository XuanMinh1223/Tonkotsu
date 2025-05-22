package com.nightfire.tonkotsu.core.domain.model

data class AnimeOverview(
    val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Double?,
    val type: String?,
    val episodes: Int?,
    val synopsis: String?
)