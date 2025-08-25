package com.nightfire.tonkotsu.core.domain.util

enum class AnimeStatus (val apiValue: String?) {
    UNSPECIFIED(null),
    AIRING("airing"),
    COMPLETE("complete"),
    UPCOMING("upcoming");

    companion object {
        fun fromApiValue(value: String?): AnimeStatus =
            entries.firstOrNull { it.apiValue == value } ?: UNSPECIFIED
    }
}