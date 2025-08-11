package com.nightfire.tonkotsu.core.domain.model

data class Recommendation(
    val malId: Int,
    val title: String,
    val imageUrl: String,
    val votes: Int?
)