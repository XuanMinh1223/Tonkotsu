package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeSearchResponseDto(
    @SerializedName("data") val data: List<AnimeDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)