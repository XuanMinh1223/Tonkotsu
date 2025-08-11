package com.nightfire.tonkotsu.core.domain.model

data class Character(
    val malId: Int,
    val name: String,
    val imageUrl: String?,
    val role: String,
    val voiceActors: List<VoiceActor>?
)