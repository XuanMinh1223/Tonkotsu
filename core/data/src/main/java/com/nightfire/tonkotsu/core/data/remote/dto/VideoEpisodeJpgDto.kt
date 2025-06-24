package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoEpisodeJpgDto(
    @SerializedName("image_url") val imageUrl: String?
)