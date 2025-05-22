package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to fetch a list of top 10 anime shows.
 *
 * This class encapsulates the business logic of:
 * 1. Defining "top 10 shows" (e.g., type = "tv", limit = 10, filter = "bypopularity").
 * 2. Orchestrating the data retrieval from the [AnimeRepository].
 * 3. Emitting the state of the operation (Loading, Success, Error) as a [Flow] of [Resource].
 *
 * The `@Inject` annotation makes this class injectable by Hilt, so you can easily
 * provide it to your ViewModels.
 */
class GetTopAnimeOverviewUseCase @Inject constructor(
    private val repository: AnimeRepository // Hilt will inject AnimeRepositoryImpl here
) {
    /**
     * The 'operator fun invoke()' allows this class to be called like a function.
     * Example: `getTopAnimeOverviewUseCase()` instead of `getTopAnimeOverviewUseCase.execute()`.
     *
     * It returns a [Flow] to emit multiple values over time, representing
     * the different states of the data fetching operation (Loading, Success, Error).
     */
    operator fun invoke(): Flow<Resource<List<AnimeOverview>>> = flow {
        emit(Resource.Loading())

        val topAnimeResource = repository.getTopAnimeOverview(
            type = "tv",          // Fetch TV series specifically
            filter = "bypopularity", // Filter by popularity (common for "top" lists)
            limit = 10            // Limit to 10 results
        )

        emit(topAnimeResource)
    }
}