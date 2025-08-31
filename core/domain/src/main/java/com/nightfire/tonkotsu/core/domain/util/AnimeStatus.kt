package com.nightfire.tonkotsu.core.domain.util

enum class AnimeStatus (val apiValue: String?, val displayName: String) {
    UNSPECIFIED(null, "Any"),
    AIRING("airing", "Airing"),
    COMPLETE("complete", "Complete"),
    UPCOMING("upcoming", "Upcoming");

    companion object {
        fun fromApiValue(value: String?): AnimeStatus =
            entries.firstOrNull { it.apiValue == value } ?: UNSPECIFIED
    }
}