package com.nightfire.tonkotsu.core.domain.util

enum class AnimeType(val apiValue: String?) {
    UNSPECIFIED(null),
    TV("tv"),
    MOVIE("movie"),
    OVA("ova"),
    ONA("ona"),
    SPECIAL("special"),
    MUSIC("music"),
    CM("cm"),
    PV("pv"),
    TV_SPECIAL("tv_special");

    companion object {
        fun fromApiValue(value: String?): AnimeType =
            entries.firstOrNull { it.apiValue == value } ?: UNSPECIFIED
    }
}
