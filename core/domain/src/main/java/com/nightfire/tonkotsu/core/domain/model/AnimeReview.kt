package com.nightfire.tonkotsu.core.domain.model

import java.time.OffsetDateTime


data class AnimeReview(
    val reviewId: Int?,         // The ID of the review itself
    val reviewUrl: String?,
    val reviewType: String?,    // e.g., "anime"
    val reactions: ReviewReactions,
    val date: OffsetDateTime?,   // Parsed date for easier use
    val reviewContent: String?,
    val score: Int?,
    val user: ReviewUser,
    val reviewedAnime: ReviewedAnimeSummary // Information about the anime being reviewed
)

data class ReviewReactions(
    val overall: Int,
    val nice: Int,
    val loveIt: Int,
    val funny: Int,
    val confusing: Int,
    val informative: Int,
    val wellWritten: Int,
    val creative: Int,
)

/**
 * Represents the user who wrote the review.
 */
data class ReviewUser(
    val username: String,
    val profileUrl: String?,
    val avatarUrl: String? // Simplified to a single avatar URL
)

/**
 * A summary of the anime that this review pertains to.
 */
data class ReviewedAnimeSummary(
    val animeId: Int,
    val title: String,
    val imageUrl: String?, // Simplified to a single main image URL
    val animeUrl: String?
)