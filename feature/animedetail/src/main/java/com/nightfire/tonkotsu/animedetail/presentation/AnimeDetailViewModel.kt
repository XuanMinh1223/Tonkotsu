package com.nightfire.tonkotsu.animedetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeDetailUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeEpisodesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val getAnimeEpisodesUseCase: GetAnimeEpisodesUseCase,
    private val getAnimeCharactersUseCase: GetAnimeCharactersUseCase,
) : ViewModel() {

    private val _animeDetailState = MutableStateFlow<UiState<AnimeDetail>>(UiState.loading())
    val animeDetailState : StateFlow<UiState<AnimeDetail>> = _animeDetailState

    private val _animeEpisodesState = MutableStateFlow<UiState<List<AnimeEpisode>>>(UiState.loading())
    val animeEpisodesState: StateFlow<UiState<List<AnimeEpisode>>> = _animeEpisodesState

    private val _animeCharactersState = MutableStateFlow<UiState<List<Character>>>(UiState.loading())
    val animeCharactersState: StateFlow<UiState<List<Character>>> = _animeCharactersState

    fun getAnimeDetail(id: Int) {
        getAnimeDetailUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeDetailState.value = UiState.loading()
                }
                is Resource.Success -> {
                    _animeDetailState.value = result.data.let {
                        UiState.success(it)
                    }

                    getAnimeEpisodes(id)
                    getCharacters(id)
                }
                is Resource.Error -> {
                    _animeDetailState.value = UiState.error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getAnimeEpisodes(animeId: Int) {
        getAnimeEpisodesUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeEpisodesState.value = UiState.loading()
                }
                is Resource.Success -> {
                    _animeEpisodesState.value = result.data.let {
                        UiState.success(it)
                    }
                }
                is Resource.Error -> {
                    _animeEpisodesState.value = UiState.error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCharacters(animeId: Int) {
        getAnimeCharactersUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeCharactersState.value = UiState.loading()
                }
                is Resource.Success -> {
                    _animeCharactersState.value = result.data.let {
                        UiState.success(it)
                    }
                }
                is Resource.Error -> {
                    _animeCharactersState.value = UiState.error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}