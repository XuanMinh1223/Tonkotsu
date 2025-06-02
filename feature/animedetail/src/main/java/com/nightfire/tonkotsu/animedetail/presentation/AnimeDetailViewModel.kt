package com.nightfire.tonkotsu.animedetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(private val getAnimeDetailUseCase: GetAnimeDetailUseCase) : ViewModel() {
    private val _animeDetailState = MutableStateFlow(UiState<AnimeDetail>())
    val animeDetailState : StateFlow<UiState<AnimeDetail>> = _animeDetailState

    fun getAnimeDetail(id: Int) {
        getAnimeDetailUseCase(id).onEach { result ->
            when(result) {
                is Resource.Loading -> _animeDetailState.value = UiState.loading(result.data)
                is Resource.Success<*> -> _animeDetailState.value = result.data?.let {
                    UiState.success(it)
                } ?: UiState.error(message = result.message ?: "An unexpected error occurred",)
                is Resource.Error<*> -> UiState.error(
                    message = result.message ?: "An unexpected error occurred",
                    data = result.data
                )
            }
        }.launchIn(viewModelScope)
    }
}