// core/domain/src/main/java/com/nightfire.tonkotsu.core.domain.repository/AnimeRepository.kt
package com.nightfire.tonkotsu.core.domain.repository

import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.common.Resource // We'll define this common class next
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for anime-related data operations.
 * This lives in the domain layer to specify what data operations are needed,
 * independent of how the data is sourced (e.g., from API or database).
 */
interface AnimeRepository {

    /**
     * Retrieves a list of top anime overviews.
     * The parameters match what the Jikan API offers for flexibility.
     *
     * @param type (Optional) Filter by anime type (e.g., "tv", "movie", "ova").
     * @param filter (Optional) Filter by status (e.g., "airing", "upcoming", "bypopularity").
     * @param page (Optional) The page number for pagination.
     * @param limit (Optional) The number of results per page.
     * @return A [Resource] sealed class that wraps either a success (List<AnimeOverview>)
     * or a failure (error message).
     */
    fun getTopAnimeOverview(
        type: String? = null,
        filter: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): Flow<Resource<List<AnimeOverview>>>

    fun getAnimeDetail(
        id: Int
    ) : Flow<Resource<AnimeDetail>>

}