package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisodeDetail
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeEpisodeDetailsUseCase @Inject constructor(
    private val repository: TonkotsuRepository // Inject the repository interface
) {
    operator fun invoke(malId: Int, episodeId: Int): Flow<Resource<AnimeEpisodeDetail>> {
        return repository.getAnimeEpisodeDetail(malId, episodeId)
    }
}