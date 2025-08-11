package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.ReviewReactions

data class ReviewReactionsDto(
    @SerializedName("overall") val overall: Int?,
    @SerializedName("nice") val nice: Int?,
    @SerializedName("love_it") val loveIt: Int?,
    @SerializedName("funny") val funny: Int?,
    @SerializedName("confusing") val confusing: Int?,
    @SerializedName("informative") val informative: Int?,
    @SerializedName("well_written") val wellWritten: Int?,
    @SerializedName("creative") val creative: Int?
)

fun ReviewReactionsDto?.toReviewReactions(): ReviewReactions {
    return ReviewReactions(
        overall = this?.overall ?: 0,
        nice = this?.nice ?: 0,
        loveIt = this?.loveIt ?: 0,
        funny = this?.funny ?: 0,
        confusing = this?.confusing ?: 0,
        informative = this?.informative ?: 0,
        wellWritten = this?.wellWritten ?: 0,
        creative = this?.creative ?: 0
    )
}