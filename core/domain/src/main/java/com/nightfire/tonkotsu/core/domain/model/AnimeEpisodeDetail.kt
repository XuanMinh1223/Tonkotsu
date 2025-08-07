package com.nightfire.tonkotsu.core.domain.model

data class AnimeEpisodeDetail(
    val id: Int,
    val url: String,
    val title: String,
    val titleJapanese: String,
    val titleRomanji: String,
    val duration: Int,
    val airedDate: String,
    val isFiller: Boolean,
    val isRecap: Boolean,
    val synopsis: String
)