package com.nightfire.tonkotsu.animedetail.presentation // Adjust your package as needed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState // Import your new UiState sealed class
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeDetailUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeEpisodesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeCharactersUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeImagesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeReviewsUseCase
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
    private val getAnimeReviewsUseCase: GetAnimeReviewsUseCase,
) : ViewModel() {

    // Initialize with UiState.Loading() for each state flow
    private val _animeDetailState = MutableStateFlow<UiState<AnimeDetail>>(UiState.Loading())
    val animeDetailState : StateFlow<UiState<AnimeDetail>> = _animeDetailState

    private val _animeEpisodesState = MutableStateFlow<UiState<List<AnimeEpisode>>>(UiState.Loading()) // Changed to Episode
    val animeEpisodesState: StateFlow<UiState<List<AnimeEpisode>>> = _animeEpisodesState

    private val _animeCharactersState = MutableStateFlow<UiState<List<Character>>>(UiState.Loading())
    val animeCharactersState: StateFlow<UiState<List<Character>>> = _animeCharactersState

    private val _animeImagesState = MutableStateFlow<UiState<List<Image>>>(UiState.Loading())
    val animeImagesState: StateFlow<UiState<List<Image>>> = _animeImagesState

    private val _animeVideosState = MutableStateFlow<UiState<List<Video>>>(UiState.Loading())
    val animeVideosState : StateFlow<UiState<List<Video>>> = _animeVideosState

    fun getAnimeDetail(id: Int) {
        getAnimeDetailUseCase(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Use UiState.Loading data class constructor, pass existing data if available
                    _animeDetailState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    // UiState.Success expects non-null data. Handle null data from Resource.Success.
                    _animeDetailState.value = result.data.let {
                        UiState.Success(it)
                    }

                    // Only fetch related data if animeDetail was successfully loaded (i.e., result.data is not null)
                    getAnimeEpisodes(id)
                    getCharacters(id)
                    getImages(id)
                    getVideos(id)
                    getReviews(id)
                }
                is Resource.Error -> {
                    // Use UiState.Error data class constructor
                    _animeDetailState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        // Assuming Resource.Error has an isRetrying property
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
                    _animeEpisodesState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _animeEpisodesState.value = result.data.let {
                        UiState.Success(it)
                    }
                }
                is Resource.Error -> {
                    _animeEpisodesState.value = UiState.Error(
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
                    _animeCharactersState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _animeCharactersState.value = result.data.let {
                        UiState.Success(it)
                    }
                }
                is Resource.Error -> {
                    _animeCharactersState.value = UiState.Error(
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
                    _animeImagesState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _animeImagesState.value = result.data.let {
                        UiState.Success(it)
                    }
                }
                is Resource.Error -> {
                    _animeImagesState.value = UiState.Error(
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
                    _animeVideosState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _animeVideosState.value = result.data?.let {
                        UiState.Success(it)
                    } ?: UiState.Error(
                        message =  "Videos data is null after successful load.",
                        data = null,
                        isRetrying = false
                    )
                }
                is Resource.Error -> {
                    _animeVideosState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        isRetrying = result.isRetrying
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getReviews(animeId: Int) {
        getAnimeReviewsUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {

                }

                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}
