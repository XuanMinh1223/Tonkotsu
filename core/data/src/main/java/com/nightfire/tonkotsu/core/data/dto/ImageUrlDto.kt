package com.nightfire.tonkotsu.core.data.dto

import com.google.gson.annotations.SerializedName

data class ImageUrlDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)