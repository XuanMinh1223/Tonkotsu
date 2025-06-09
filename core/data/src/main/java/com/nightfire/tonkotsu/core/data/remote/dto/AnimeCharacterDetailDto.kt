package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeCharacterDetailDto (
    @SerializedName("mal_id") val id: Int,
    @SerializedName("url") val url: String,
    @SerializedName("images") val images: ImagesDto,
    @SerializedName("name") val name: String,
)