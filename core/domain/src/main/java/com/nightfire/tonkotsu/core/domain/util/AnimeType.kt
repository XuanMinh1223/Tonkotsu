package com.nightfire.tonkotsu.core.domain.util

enum class AnimeType(val apiValue: String?, val displayName: String) {
    UNSPECIFIED(null, "Any"),
    TV("tv", "Television"),
    MOVIE("movie", "Movie"),
    OVA("ova", "Original Video Animation"),
    ONA("ona", "Original Net Animation"),
    SPECIAL("special", "Special"),
    MUSIC("music", "Music Video"),
    CM("cm", "Commercial"),
    PV("pv", "Promotional"),
    TV_SPECIAL("tv_special", "TV Special");

    companion object {
        fun fromApiValue(value: String?): AnimeType =
            entries.firstOrNull { it.apiValue == value } ?: UNSPECIFIED
    }
}
