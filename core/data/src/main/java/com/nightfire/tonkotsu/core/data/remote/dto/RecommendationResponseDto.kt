package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecommendationResponseDto (
    @SerializedName("data") val data: List<RecommendationEntryDto>
)