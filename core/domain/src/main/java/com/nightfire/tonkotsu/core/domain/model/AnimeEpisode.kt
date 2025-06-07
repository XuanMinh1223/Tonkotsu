package com.nightfire.tonkotsu.core.domain.model

import java.time.LocalDate

data class AnimeEpisode(
    val malId: Int,
    val title: String,
    val titleJapanese: String?,
    val titleRomanji: String?,
    val airedDate: LocalDate?, // Mapped from 'aired' string in DTO
    val score: Double?,
    val isFiller: Boolean, // Mapped from 'filler'
    val isRecap: Boolean,  // Mapped from 'recap'
    val episodeUrl: String?, // Mapped from 'url'
    val forumUrl: String?
)