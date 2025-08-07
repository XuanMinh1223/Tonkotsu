package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponse(
    @SerializedName("data") val data: AnimeDetailDto
)