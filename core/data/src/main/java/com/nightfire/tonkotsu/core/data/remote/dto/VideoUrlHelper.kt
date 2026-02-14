package com.nightfire.tonkotsu.core.data.remote.dto

fun convertToStandardYouTubeUrl(embedUrl: String?): String? {
    if (embedUrl.isNullOrBlank()) return null
    val videoId = embedUrl.substringAfterLast("/").substringBefore("?")
    return "https://www.youtube.com/watch?v=$videoId"
}

fun generateYouTubeThumbnailUrl(embedUrl: String?): String? {
    if (embedUrl.isNullOrBlank()) return null
    val videoId = embedUrl.substringAfterLast("/").substringBefore("?")
    return "https://img.youtube.com/vi/$videoId/0.jpg"
}