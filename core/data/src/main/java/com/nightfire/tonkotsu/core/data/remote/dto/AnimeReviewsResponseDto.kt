package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

class AnimeReviewsResponseDto (
    @SerializedName("data") val data: List<AnimeReviewDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)