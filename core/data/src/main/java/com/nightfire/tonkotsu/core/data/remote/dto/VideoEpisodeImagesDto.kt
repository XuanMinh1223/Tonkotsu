package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoEpisodeImagesDto(
    @SerializedName("jpg") val jpg: VideoEpisodeJpgDto?
)