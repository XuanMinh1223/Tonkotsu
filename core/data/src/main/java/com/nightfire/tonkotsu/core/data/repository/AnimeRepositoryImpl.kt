package com.nightfire.tonkotsu.core.data.repository


import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview   // Ensure this is imported
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.networkBoundResourceFlow
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeDetailResponse
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeEpisodesResponse
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeReviewsResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeVideosResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.CharactersResponse
import com.nightfire.tonkotsu.core.data.remote.dto.ImagesResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.TopAnimeResponse
import com.nightfire.tonkotsu.core.data.remote.dto.VideoDataDto
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeDetail
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeEpisode
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeOverview
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeReview
import com.nightfire.tonkotsu.core.data.remote.dto.toCharacter
import com.nightfire.tonkotsu.core.data.remote.dto.toImage
import com.nightfire.tonkotsu.core.data.remote.dto.toVideoList
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the [AnimeRepository] interface.
 * This class handles fetching anime data from the Jikan API and mapping it to domain models.
 * It also wraps the results in a [Resource] sealed class for state management.
 */
class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApi
) : AnimeRepository {

    override fun getTopAnimeOverview(
        type: String?,
        filter: String?,
        page: Int?,
        limit: Int?
    ): Flow<Resource<List<AnimeOverview>>> {
        return networkBoundResourceFlow(
            apiCall = {
                api.getTopAnime(
                    type = type,
                    filter = filter,
                    page = page,
                    limit = limit
                )
            },
            mapper = { dto: TopAnimeResponse ->
                dto.data.map { it.toAnimeOverview() }
            }
        )
    }

    override fun getAnimeDetail(id: Int): Flow<Resource<AnimeDetail>> {
        return networkBoundResourceFlow(
            apiCall = { api.getAnimeDetail(id) },
            mapper = { dto: AnimeDetailResponse ->
                dto.data.toAnimeDetail()
            }
        )
    }

    override fun getAnimeEpisodes(id: Int): Flow<Resource<List<AnimeEpisode>>> {
        return networkBoundResourceFlow(
            apiCall = { api.getAnimeEpisodes(id)},
            mapper = {dto: AnimeEpisodesResponse ->
                dto.data.map { it.toAnimeEpisode() }
            }
        )
    }

    override fun getAnimeCharacters(id: Int): Flow<Resource<List<Character>>> {
        return networkBoundResourceFlow(
            apiCall = {api.getAnimeCharacters(id)},
            mapper = { dto: CharactersResponse ->
                dto.data.map { it.toCharacter() }
            }
        )
    }

    override fun getAnimeImages(id: Int): Flow<Resource<List<Image>>> {
        return networkBoundResourceFlow(
            apiCall = { api.getAnimeImages(id) },
            mapper = { dto: ImagesResponseDto ->
                dto.data.map { it.toImage() }
            }
        )
    }

    override fun getAnimeVideos(id: Int): Flow<Resource<List<Video>>> {
        return networkBoundResourceFlow(
            apiCall = { api.getAnimeVideos(id) },
            mapper = { dto: AnimeVideosResponseDto ->
                dto.data?.toVideoList() ?: emptyList()
            }
        )
    }

    override fun getAnimeReviews(id: Int): Flow<Resource<List<AnimeReview>>> {
        return networkBoundResourceFlow(
            apiCall = { api.getAnimeReviews(id)},
            mapper = { dto: AnimeReviewsResponseDto ->
                dto.data.map { it.toAnimeReview() }
            }
        )
    }
}