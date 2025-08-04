package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.ReviewUser

data class ReviewUserDto(
    @SerializedName("url") val url: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("images") val images: ImagesDto?
)

fun ReviewUserDto?.toReviewUser(): ReviewUser {
    val defaultUsername = "Anonymous"
    return ReviewUser(
        username = this?.username ?: defaultUsername,
        profileUrl = this?.url ?: "",
        avatarUrl = this?.images?.jpg?.imageUrl ?: this?.images?.webp?.imageUrl
    )
}