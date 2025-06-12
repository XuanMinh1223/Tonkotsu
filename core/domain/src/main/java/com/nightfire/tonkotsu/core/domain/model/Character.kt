package com.nightfire.tonkotsu.core.domain.model

data class Character(
    val characterDetail: CharacterDetail,
    val role: String,
    val voiceActors: List<VoiceActor>?
)