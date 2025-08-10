package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeNewsResponseDto(
    @SerializedName("data") val data: List<NewsDto>,
    @SerializedName("pagination") val pagination: PaginationDto,
)