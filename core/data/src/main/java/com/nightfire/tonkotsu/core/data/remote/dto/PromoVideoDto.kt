package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Video

data class PromoVideoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("trailer") val trailer: VideoDetailsDto? // Reusing VideoDetailsDto
)

fun PromoVideoDto.toVideo(): Video? {
    val videoDetails = this.trailer
    val videoUrl = videoDetails?.embedUrl
    val thumbnailUrl = videoDetails?.images?.smallImageUrl
        ?: videoDetails?.images?.imageUrl

    return if (!videoUrl.isNullOrBlank()) {
        Video(
            videoUrl = videoUrl,
            thumbnailUrl = thumbnailUrl
        )
    } else {
        null
    }
}