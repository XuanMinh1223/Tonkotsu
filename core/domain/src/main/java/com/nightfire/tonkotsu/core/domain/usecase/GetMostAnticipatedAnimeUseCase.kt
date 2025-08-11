package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import com.nightfire.tonkotsu.core.domain.util.AnimeFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostAnticipatedAnimeUseCase @Inject constructor(
    private val repository: TonkotsuRepository
) {
    operator fun invoke(): Flow<Resource<List<AnimeOverview>>> {
        return repository.getTopAnimeOverview(
            filter = AnimeFilter.UPCOMING.apiValue,
            limit = 10
        )
    }
}