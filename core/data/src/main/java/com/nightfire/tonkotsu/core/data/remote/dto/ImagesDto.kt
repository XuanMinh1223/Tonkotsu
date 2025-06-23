package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Image

data class ImagesDto(
    @SerializedName("jpg") val jpg: ImageUrlDto?,
    @SerializedName("webp") val webp: ImageUrlDto?
)
fun ImagesDto.toImage(): Image {
    val imageUrl = this.webp?.largeImageUrl // Try large WebP first
        ?: this.jpg?.largeImageUrl        // Fallback to large JPG
        ?: this.webp?.imageUrl            // Fallback to any WebP
        ?: this.jpg?.imageUrl                            // Default to empty string if no URL found

    return Image(url = imageUrl)
}
