package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeEpisodesResponse(
    @SerializedName("data") val data: List<AnimeEpisodeDto>,
    @SerializedName("pagination") val pagination: PaginationDto
)