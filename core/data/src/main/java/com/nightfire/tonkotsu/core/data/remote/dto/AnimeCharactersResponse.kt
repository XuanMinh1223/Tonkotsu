package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeCharactersResponse(
    @SerializedName("data") val data: List<AnimeCharacterDto>
)
