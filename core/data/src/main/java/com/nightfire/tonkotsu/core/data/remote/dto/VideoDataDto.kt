package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Video

data class VideoDataDto(
    @SerializedName("promo") val promo: List<PromoVideoDto>?,
    @SerializedName("episodes") val episodes: List<VideoEpisodeDto>?,
    @SerializedName("music_videos") val musicVideos: List<MusicVideoDto>?
)

fun VideoDataDto.toVideoList(): List<Video> {
    val promoVideos = this.promo?.mapNotNull { it.toVideo() } ?: emptyList()
    val musicVideos = this.musicVideos?.mapNotNull { it.toVideo() } ?: emptyList()

    return promoVideos + musicVideos
}