package com.nightfire.tonkotsu.animedetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeDetailUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeEpisodesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeCharactersUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeImagesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeVideosUseCase
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
    private val getAnimeImagesUseCase: GetAnimeImagesUseCase,
    private val getAnimeVideosUseCase: GetAnimeVideosUseCase,
) : ViewModel() {

    private val _animeDetailState = MutableStateFlow<UiState<AnimeDetail>>(UiState.loading())
    val animeDetailState : StateFlow<UiState<AnimeDetail>> = _animeDetailState

    private val _animeEpisodesState = MutableStateFlow<UiState<List<AnimeEpisode>>>(UiState.loading())
    val animeEpisodesState: StateFlow<UiState<List<AnimeEpisode>>> = _animeEpisodesState

    private val _animeCharactersState = MutableStateFlow<UiState<List<Character>>>(UiState.loading())
    val animeCharactersState: StateFlow<UiState<List<Character>>> = _animeCharactersState

    private val _animeImagesState = MutableStateFlow<UiState<List<Image>>>(UiState.loading())
    val animeImagesState: StateFlow<UiState<List<Image>>> = _animeImagesState

    private val _animeVideosState = MutableStateFlow<UiState<List<Video>>>(UiState.loading())
    val animeVideosState : StateFlow<UiState<List<Video>>> = _animeVideosState

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
                    getImages(id)
                    getVideos(id)
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

    private fun getImages(animeId: Int) {
        getAnimeImagesUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeImagesState.value = UiState.loading()
                }
                is Resource.Success -> {
                    _animeImagesState.value = UiState.success(result.data)
                }
                is Resource.Error -> {
                    _animeImagesState.value = UiState.error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getVideos(animeId: Int) {
        getAnimeVideosUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeVideosState.value = UiState.loading()
                }
                is Resource.Success -> {
                    _animeVideosState.value = UiState.success(result.data)
                }
                is Resource.Error -> {
                    _animeVideosState.value = UiState.error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}