package com.nightfire.tonkotsu.core.domain.usecase

import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.repository.TonkotsuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeCharactersUseCase @Inject constructor(
    private val repository: TonkotsuRepository // Inject the repository interface
) {
    operator fun invoke(malId: Int): Flow<Resource<List<Character>>> {
        return repository.getAnimeCharacters(malId)
    }
}