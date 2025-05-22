package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class ImagesDto(
    @SerializedName("jpg") val jpg: ImageUrlDto?,
    @SerializedName("webp") val webp: ImageUrlDto?
)
