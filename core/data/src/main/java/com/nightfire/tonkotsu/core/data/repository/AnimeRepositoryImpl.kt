package com.nightfire.tonkotsu.core.data.repository


import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview   // Ensure this is imported
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.networkBoundResourceFlow
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeDetailResponse
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeEpisodesResponse
import com.nightfire.tonkotsu.core.data.remote.dto.TopAnimeResponse
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeDetail
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeEpisode
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeOverview
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
}