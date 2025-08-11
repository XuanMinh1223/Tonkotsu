package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VoiceActorDto (
    @SerializedName("person") val person: PersonDto,
    @SerializedName("language") val language: String,
)