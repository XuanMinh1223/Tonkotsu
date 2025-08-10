package com.nightfire.tonkotsu.core.domain.model

data class News(
    val id: Int,
    val url: String,
    val title: String,
    val date: String,
    val authorUsername: String,
    val authorUrl: String,
    val forumUrl: String,
    val imageUrl: String?,
    val comments: Int,
    val excerpt: String
)