package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeCharacterDto(
    @SerializedName("character") val characterDetailDto: AnimeCharacterDetailDto,
    @SerializedName("role") val role: String,
    @SerializedName("voice_actors") val voiceActors: List<AnimeVoiceActorDto>,
    )
