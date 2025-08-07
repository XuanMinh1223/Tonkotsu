package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisodeDetail

data class AnimeEpisodeDetailDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("title_japanese") val titleJapanese: String,
    @SerializedName("title_romanji") val titleRomanji: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("aired") val aired: String,
    @SerializedName("filler") val filler: Boolean,
    @SerializedName("recap") val recap: Boolean,
    @SerializedName("synopsis") val synopsis: String
)

fun AnimeEpisodeDetailDto.toAnimeEpisodeDetail(): AnimeEpisodeDetail {
    return AnimeEpisodeDetail(
        id = malId,
        url = url,
        title = title,
        titleJapanese = titleJapanese,
        titleRomanji = titleRomanji,
        duration = duration,
        airedDate = aired,
        isFiller = filler,
        isRecap = recap,
        synopsis = synopsis
    )
}
