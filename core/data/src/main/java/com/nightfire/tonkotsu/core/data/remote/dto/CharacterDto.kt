package com.nightfire.tonkotsu.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.CharacterDetail
import com.nightfire.tonkotsu.core.domain.model.VoiceActor

data class CharacterDto(
    @SerializedName("character") val characterDetailDto: CharacterDetailDto,
    @SerializedName("role") val role: String,
    @SerializedName("voice_actors") val voiceActors: List<VoiceActorDto>?,
    )
fun CharacterDto.toCharacter(): Character {
    val characterDto = this.characterDetailDto
    val role = this.role

    val characterDomain = CharacterDetail(
        malId = characterDto.id,
        name = characterDto.name,
        imageUrl = characterDto.images.webp?.largeImageUrl
            ?: characterDto.images.jpg?.largeImageUrl
            ?: characterDto.images.webp?.imageUrl
            ?: characterDto.images.jpg?.imageUrl
    )

    val voiceActorsDomain = this.voiceActors?.map { vaDto ->
        val personDto = vaDto.person
        VoiceActor(
            malId = personDto.id,
            name = personDto.name,
            imageUrl = personDto.images.jpg?.imageUrl,
            language = vaDto.language
        )
    }

    return Character(
        malId = characterDomain.malId,
        name = characterDomain.name,
        imageUrl = characterDomain.imageUrl,
        role = role,
        voiceActors = voiceActorsDomain
    )
}

