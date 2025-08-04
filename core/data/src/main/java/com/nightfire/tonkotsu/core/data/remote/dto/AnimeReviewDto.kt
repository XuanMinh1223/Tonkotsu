package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.common.utils.parseDateTimeString
import com.nightfire.tonkotsu.core.domain.model.AnimeReview

data class AnimeReviewDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("reactions") val reactions: ReviewReactionsDto?,
    @SerializedName("date") val date: String?,
    @SerializedName("review") val review: String?,
    @SerializedName("score") val score: Int?,
    @SerializedName("user") val user: ReviewUserDto?,
    @SerializedName("anime") val anime: AnimeSummaryDto?
)

fun AnimeReviewDto.toAnimeReview(): AnimeReview {

    return AnimeReview(
        reviewId = this.malId,
        reviewUrl = this.url,
        reviewType = this.type,
        reactions = this.reactions.toReviewReactions(),
        date = parseDateTimeString(this.date),
        reviewContent = this.review,
        score = this.score,
        user = this.user.toReviewUser(),
        reviewedAnime = this.anime.toReviewedAnimeSummary()
    )
}