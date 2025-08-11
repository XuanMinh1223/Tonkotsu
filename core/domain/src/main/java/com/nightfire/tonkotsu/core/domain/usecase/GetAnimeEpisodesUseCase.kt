package com.nightfire.tonkotsu.core.domain.usecase

import androidx.paging.PagingData
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeEpisodesUseCase @Inject constructor(
    private val repository: TonkotsuRepository // Inject the repository interface
) {
    operator fun invoke(malId: Int): Flow<PagingData<AnimeEpisode>> {
        return repository.getAnimeEpisodes(malId)
    }
}