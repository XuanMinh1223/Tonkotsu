package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Video

data class MusicVideoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("video") val video: VideoDetailsDto?, // Reusing VideoDetailsDto
    @SerializedName("meta") val meta: MusicVideoMetaDto?
)

fun MusicVideoDto.toVideo(): Video? {
    val videoDetails = this.video
    val videoUrl = convertToStandardYouTubeUrl(videoDetails?.embedUrl)
    val thumbnailUrl = videoDetails?.images?.smallImageUrl
        ?: videoDetails?.images?.imageUrl // Fallback to a general image_url if small isn't available
        ?: generateYouTubeThumbnailUrl(videoDetails?.embedUrl)

    return if (!videoUrl.isNullOrBlank()) {
        Video(
            videoUrl = videoUrl,
            thumbnailUrl = thumbnailUrl
        )
    } else {
        null // Return null if the video URL is missing or blank
    }
}