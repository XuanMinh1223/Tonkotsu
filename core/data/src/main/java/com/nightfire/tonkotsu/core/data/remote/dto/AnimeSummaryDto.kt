package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.ReviewedAnimeSummary

data class AnimeSummaryDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("images") val images: ImagesDto?,
    @SerializedName("title") val title: String?
)

fun AnimeSummaryDto?.toReviewedAnimeSummary(): ReviewedAnimeSummary {
    val defaultTitle = "Unknown Anime"
    val defaultAnimeId = -1

    return ReviewedAnimeSummary(
        animeId = this?.malId ?: defaultAnimeId,
        title = this?.title ?: defaultTitle,
        imageUrl = this?.images?.jpg?.imageUrl ?: this?.images?.webp?.imageUrl,
        animeUrl = this?.url ?: ""
    )
}