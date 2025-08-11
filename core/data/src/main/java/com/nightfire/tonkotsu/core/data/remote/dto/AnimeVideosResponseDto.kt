package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeVideosResponseDto(
    @SerializedName("data") val data: VideoDataDto?
)