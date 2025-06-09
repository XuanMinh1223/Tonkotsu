package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeVoiceActorDto (
    @SerializedName("person") val person: PersonDto,
    @SerializedName("language") val language: String,
)