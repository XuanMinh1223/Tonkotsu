package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class TopAnimeResponse(
    @SerializedName("data") val data: List<AnimeDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)