package com.nightfire.tonkotsu.core.domain.util

enum class AnimeRating(val apiValue: String?, val description: String) {
    UNSPECIFIED(null, "All ratings"),
    G("g", "All Ages"),
    PG("pg", "Children"),
    PG13("pg13", "Teens 13 or older"),
    R17("r17", "17+ (violence & profanity)"),
    R("r", "Mild Nudity"),
    RX("rx", "Hentai");

    companion object {
        fun fromApiValue(value: String?): AnimeRating =
            entries.firstOrNull { it.apiValue == value } ?: UNSPECIFIED
    }
}