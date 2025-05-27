package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository
import com.nightfire.tonkotsu.core.domain.util.AnimeFilter
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
    private val repository: AnimeRepository
) {
    /**
     * The 'operator fun invoke()' allows this class to be called like a function.
     * Example: `getTopAnimeOverviewUseCase()` instead of `getTopAnimeOverviewUseCase.execute()`.
     *
     * It returns a [Flow] to emit multiple values over time, representing
     * the different states of the data fetching operation (Loading, Success, Error).
     */
    operator fun invoke(): Flow<Resource<List<AnimeOverview>>> {
        return repository.getTopAnimeOverview(
            filter = AnimeFilter.BY_POPULARITY.apiValue,
            limit = 10
        )
    }
}