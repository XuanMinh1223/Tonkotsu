package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: AnimeRepository // Inject the repository interface
) {
    operator fun invoke(malId: Int): Flow<Resource<AnimeDetail>> {
        return repository.getAnimeDetail(malId)
    }
}