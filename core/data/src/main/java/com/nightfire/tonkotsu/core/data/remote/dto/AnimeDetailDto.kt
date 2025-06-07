package com.nightfire.tonkotsu.core.data.remote.dto

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.nightfire.tonkotsu.core.common.utils.parseDateString
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.core.domain.model.NavigableLink
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class AnimeDetailDto(
     @SerializedName("mal_id") val malId: Int,
     @SerializedName("url") val url: String?,
     @SerializedName("images") val images: ImagesDto?,
     @SerializedName("trailer") val trailer: TrailerDto?,
     @SerializedName("titles") val titles: List<TitleDto>?,
     @SerializedName("title") val title: String?,
     @SerializedName("title_english") val titleEnglish: String?,
     @SerializedName("title_japanese") val titleJapanese: String?,
     @SerializedName("type") val type: String?,
     @SerializedName("source") val source: String?,
     @SerializedName("episodes") val episodes: Int?,
     @SerializedName("status") val status: String?,
     @SerializedName("airing") val airing: Boolean?,
     @SerializedName("aired") val aired: AiredDto?,
     @SerializedName("duration") val duration: String?,
     @SerializedName("rating") val rating: String?,
     @SerializedName("score") val score: Double?,
     @SerializedName("scored_by") val scoredBy: Int?,
     @SerializedName("rank") val rank: Int?,
     @SerializedName("popularity") val popularity: Int?,
     @SerializedName("members") val members: Int?,
     @SerializedName("favorites") val favorites: Int?,
     @SerializedName("synopsis") val synopsis: String?,
     @SerializedName("background") val background: String?,
     @SerializedName("season") val season: String?,
     @SerializedName("year") val year: Int?,
     @SerializedName("broadcast") val broadcast: BroadcastDto?,
     @SerializedName("producers") val producers: List<StaffDto>?,
     @SerializedName("licensors") val licensors: List<StaffDto>?,
     @SerializedName("studios") val studios: List<StaffDto>?,
     @SerializedName("genres") val genres: List<MalSubEntityDto>?,
     @SerializedName("explicit_genres") val explicitGenres: List<MalSubEntityDto>?,
     @SerializedName("themes") val themes: List<MalSubEntityDto>?,
     @SerializedName("demographics") val demographics: List<MalSubEntityDto>?,
     @SerializedName("relations") val relations: List<RelationDto>?,
     @SerializedName("theme") val theme: ThemeSongsDto?,
     @SerializedName("external") val external: List<ExternalLinkDto>?,
     @SerializedName("streaming") val streaming: List<StreamingServiceDto>?
)

fun AnimeDetailDto.toAnimeDetail(): AnimeDetail {

     return AnimeDetail(
          id = malId,
          title = title ?: "N/A",
          alternativeTitle = if (titleEnglish != title) titleEnglish else null,
          japaneseTitle = titleJapanese,
          imageUrl = images?.jpg?.largeImageUrl
               ?: images?.webp?.largeImageUrl
               ?: images?.jpg?.imageUrl
               ?: "",
          synopsis = synopsis ?: "No synopsis available.",
          type = type,
          score = score,
          scoredBy = scoredBy,
          rank = rank,
          popularity = popularity,
          members = members,
          favorites = favorites,
          episodes = episodes,
          status = status,
          duration = duration,
          rating = rating,
          source = source,
          season = season,
          year = year,
          broadcast = broadcast?.string,
          premiereDate = parseDateString(aired?.from), // <-- Use the helper function
          endDate = parseDateString(aired?.to),
          genres = genres?.mapNotNull { it.name } ?: emptyList(),
          themes = themes?.mapNotNull { it.name } ?: emptyList(),
          categories = demographics?.mapNotNull { it.name } ?: emptyList(),
          studios = studios?.mapNotNull { it.name } ?: emptyList(),
          producers = producers?.mapNotNull { it.name } ?: emptyList(),
          licensors = licensors?.mapNotNull { it.name } ?: emptyList(),
          background = background,
          trailerYoutubeId = trailer?.youtubeId,
          relations = relations?.associate { relationDto ->
               relationDto.relation!! to (relationDto.entry?.map { entryDto ->
                    RelationEntry(
                         id = entryDto.malId ?: 0, // Default ID if null
                         name = entryDto.name ?: "Unknown",
                         type = entryDto.type ?: "Unknown",
                         url = entryDto.url ?: ""
                    )
               } ?: emptyList())
          } ?: emptyMap(),
          streamingLinks = streaming?.map { streamingDto ->
               NavigableLink(
                    name = streamingDto.name ?: "Unknown",
                    url = streamingDto.url ?: ""
               )
          } ?: emptyList(),
          openingThemes = theme?.openings ?: emptyList(),
          endingThemes = theme?.endings ?: emptyList(),
          externalLinks = external?.map { externalDto ->
               NavigableLink(
                    name = externalDto.name ?: "Unknown",
                    url = externalDto.url ?: ""
               )
          } ?: emptyList()
     )
}

