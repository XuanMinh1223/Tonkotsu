package com.nightfire.tonkotsu.core.domain.usecase

import androidx.paging.PagingData
import com.nightfire.tonkotsu.core.domain.model.News
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeNewsUseCase @Inject constructor(
    private val repository: AnimeRepository // Inject the repository interface
) {
    operator fun invoke(malId: Int): Flow<PagingData<News>> {
        return repository.getAnimeNews(malId)
    }
}