package com.nightfire.tonkotsu.core.domain.model

import java.time.OffsetDateTime

data class AnimeEpisode(
    val malId: Int,
    val title: String,
    val titleJapanese: String?,
    val titleRomanji: String?,
    val airedDate: OffsetDateTime?, // Mapped from 'aired' string in DTO
    val score: Double?,
    val isFiller: Boolean, // Mapped from 'filler'
    val isRecap: Boolean,  // Mapped from 'recap'
    val episodeUrl: String?, // Mapped from 'url'
    val forumUrl: String?
)