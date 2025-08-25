package com.nightfire.tonkotsu.core.domain.model

import com.nightfire.tonkotsu.core.domain.util.AnimeRating
import com.nightfire.tonkotsu.core.domain.util.AnimeStatus
import com.nightfire.tonkotsu.core.domain.util.AnimeType

data class AnimeFilterOptions(
    val type: AnimeType = AnimeType.UNSPECIFIED,
    val minScore: Float? = 0f,
    val maxScore: Float? = 10f,
    val status: AnimeStatus? = AnimeStatus.UNSPECIFIED,
    val rating: AnimeRating? = AnimeRating.UNSPECIFIED,
    val startDate: String? = null,
    val endDate: String? = null,
)
