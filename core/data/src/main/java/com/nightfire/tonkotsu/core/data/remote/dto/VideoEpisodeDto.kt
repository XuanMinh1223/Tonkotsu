package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoEpisodeDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("episode") val episode: String?, // Note: This 'episode' field is a String in this context
    @SerializedName("images") val images: VideoEpisodeImagesDto?
)