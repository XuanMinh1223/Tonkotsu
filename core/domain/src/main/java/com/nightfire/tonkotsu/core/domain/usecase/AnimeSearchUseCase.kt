package com.nightfire.tonkotsu.core.domain.usecase

import androidx.paging.PagingData
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.model.AnimeSearchQuery
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeSearchUseCase @Inject constructor(
    private val repository: TonkotsuRepository
) {
    operator fun invoke(query: AnimeSearchQuery): Flow<PagingData<AnimeOverview>> {
        return repository.animeSearch(query)
    }
}