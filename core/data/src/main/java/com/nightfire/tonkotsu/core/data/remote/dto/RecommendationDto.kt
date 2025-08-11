package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecommendationDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("images") val images: ImagesDto, // Reusing existing ImagesDto
    @SerializedName("title") val title: String
)
