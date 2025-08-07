package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeEpisodeDetailResponseDto(
    @SerializedName("data") val data: AnimeEpisodeDetailDto
)
