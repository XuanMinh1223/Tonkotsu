package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.News

data class NewsDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,
    @SerializedName("author_username") val authorUsername: String,
    @SerializedName("author_url") val authorUrl: String,
    @SerializedName("forum_url") val forumUrl: String,
    @SerializedName("images") val images: ImagesDto,
    @SerializedName("comments") val comments: Int,
    @SerializedName("excerpt") val excerpt: String
)

fun NewsDto.toNews(): News {
    val imageUrl = images.jpg?.largeImageUrl
        ?: images.jpg?.smallImageUrl
        ?: images.jpg?.imageUrl
        ?: ""

    return News(
        id = malId,
        url = url,
        title = title,
        date = date,
        authorUsername = authorUsername,
        authorUrl = authorUrl,
        forumUrl = forumUrl,
        imageUrl = imageUrl,
        comments = comments,
        excerpt = excerpt
    )
}

