package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("type") val type: String?,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String?
)