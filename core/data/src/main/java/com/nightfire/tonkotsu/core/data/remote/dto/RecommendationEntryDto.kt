package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Recommendation

data class RecommendationEntryDto(
    @SerializedName("entry") val entry: RecommendationDto, // The anime being recommended
    @SerializedName("url") val url: String, // URL to the recommendation on MyAnimeList
    @SerializedName("votes") val votes: Int // Number of votes for this recommendation
)

fun RecommendationEntryDto.toRecommendation(): Recommendation {
    val animeDto = this.entry
    val malId = animeDto.malId
    val title = animeDto.title

    val imageUrl = animeDto.images.webp?.largeImageUrl
        ?: animeDto.images.jpg?.largeImageUrl
        ?: animeDto.images.webp?.imageUrl
        ?: animeDto.images.jpg?.imageUrl
        ?: ""

    return Recommendation(
        malId = malId,
        title = title,
        imageUrl = imageUrl,
        votes = this.votes
    )
}
