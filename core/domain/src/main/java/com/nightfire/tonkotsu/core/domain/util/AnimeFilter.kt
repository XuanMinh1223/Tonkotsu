package com.nightfire.tonkotsu.core.domain.util

enum class AnimeFilter(val apiValue: String) {
    AIRING("airing"),
    UPCOMING("upcoming"),
    BY_POPULARITY("bypopularity"),
    BY_FAVORITE("byfavorite"),
}