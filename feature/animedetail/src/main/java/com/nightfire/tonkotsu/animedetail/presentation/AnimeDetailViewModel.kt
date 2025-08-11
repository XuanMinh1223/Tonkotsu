package com.nightfire.tonkotsu.animedetail.presentation // Adjust your package as needed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState // Import your new UiState sealed class
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.News
import com.nightfire.tonkotsu.core.domain.model.Recommendation
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeDetailUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeEpisodesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeCharactersUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeImagesUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeNewsUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeRecommendationsUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeReviewsUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetAnimeVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
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
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase,
    private val getAnimeNewsUseCase: GetAnimeNewsUseCase,
) : ViewModel() {

    private val _animeId = MutableStateFlow<Int?>(null)
    val animeId: StateFlow<Int?> = _animeId

    // Initialize with UiState.Loading() for each state flow
    private val _animeDetailState = MutableStateFlow<UiState<AnimeDetail>>(UiState.Loading())
    val animeDetailState : StateFlow<UiState<AnimeDetail>> = _animeDetailState

    private val _animeCharactersState = MutableStateFlow<UiState<List<Character>>>(UiState.Loading())
    val animeCharactersState: StateFlow<UiState<List<Character>>> = _animeCharactersState

    private val _animeImagesState = MutableStateFlow<UiState<List<Image>>>(UiState.Loading())
    val animeImagesState: StateFlow<UiState<List<Image>>> = _animeImagesState

    private val _animeVideosState = MutableStateFlow<UiState<List<Video>>>(UiState.Loading())
    val animeVideosState : StateFlow<UiState<List<Video>>> = _animeVideosState

    private val _animeReviewsState = MutableStateFlow<UiState<List<AnimeReview>>>(UiState.Loading())
    val animeReviewsState : StateFlow<UiState<List<AnimeReview>>> = _animeReviewsState

    private val _animeRecommendationsState = MutableStateFlow<UiState<List<Recommendation>>>(UiState.Loading())
    val animeRecommendationsState : StateFlow<UiState<List<Recommendation>>> = _animeRecommendationsState

    @OptIn(ExperimentalCoroutinesApi::class)
    val animeEpisodes: Flow<PagingData<AnimeEpisode>> = _animeId
        .filterNotNull()
        .flatMapLatest { id ->
            getAnimeEpisodesUseCase(id)
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val animeReviews: Flow<PagingData<AnimeReview>> = _animeId
        .filterNotNull()
        .flatMapLatest { id ->
            getAnimeReviewsUseCase(id)
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val animeNews: Flow<PagingData<News>> = _animeId
        .filterNotNull()
        .flatMapLatest { id ->
            getAnimeNewsUseCase(id)
        }
        .cachedIn(viewModelScope)

    private fun setAnimeId(id: Int) {
        _animeId.value = id
    }

    fun getAnimeDetail(id: Int) {
        setAnimeId(id)
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

                    getCharacters(id)
                    getImages(id)
                    getVideos(id)
                    getRecommendations(id)
                }
                is Resource.Error -> {
                    _animeDetailState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onRelationClick(relationEntry: RelationEntry) {
        when (relationEntry.type) {
            "anime" -> getAnimeDetail(id = relationEntry.id)
        }
    }

    fun onRecommendationClick(malId: Int) {
        getAnimeDetail(id = malId)
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
                    _animeVideosState.value = result.data.let {
                        UiState.Success(it)
                    }
                }
                is Resource.Error -> {
                    _animeVideosState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecommendations(animeId: Int) {
        getAnimeRecommendationsUseCase(animeId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _animeRecommendationsState.value = UiState.Loading()
                }

                is Resource.Success -> {
                    _animeRecommendationsState.value = UiState.Success(result.data)
                }

                is Resource.Error -> {
                    _animeRecommendationsState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        )
                }
            }
        }.launchIn(viewModelScope)
    }
}
