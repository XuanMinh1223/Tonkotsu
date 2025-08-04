package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.common.utils.parseDateTimeString
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode

data class AnimeEpisodeDto(
    @SerializedName("mal_id") val malId: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("title_japanese") val titleJapanese: String?,
    @SerializedName("title_romanji") val titleRomanji: String?,
    @SerializedName("aired") val aired: String?, // Represents the ISO 8601 formatted date-time string
    @SerializedName("score") val score: Double?,
    @SerializedName("filler") val filler: Boolean?,
    @SerializedName("recap") val recap: Boolean?,
    @SerializedName("forum_url") val forumUrl: String?
)

fun AnimeEpisodeDto.toAnimeEpisode(): AnimeEpisode {

    return AnimeEpisode(
        malId = malId
            ?: 0, // Provide a default if malId can be null, or make malId non-nullable in domain
        title = title ?: "N/A", // Default title if null
        titleJapanese = titleJapanese,
        titleRomanji = titleRomanji,
        airedDate = parseDateTimeString(aired), // Parse the 'aired' string to LocalDate
        score = score,
        isFiller = filler == true, // Default to false if filler is null
        isRecap = recap == true,   // Default to false if recap is null
        episodeUrl = url,
        forumUrl = forumUrl
    )
}