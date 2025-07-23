package com.nightfire.tonkotsu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState // Import your new UiState sealed class
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.usecase.GetMostAnticipatedAnimeUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetPopularAnimeUseCase
import com.nightfire.tonkotsu.core.domain.usecase.GetTopAiringAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularAnimeUseCase: GetPopularAnimeUseCase,
    private val getTopAiringAnimeUseCase: GetTopAiringAnimeUseCase,
    private val getMostAnticipatedAnimeUseCase: GetMostAnticipatedAnimeUseCase
) : ViewModel() {

    // Initialize with UiState.Loading() for each state flow
    private val _popularAnimeState = MutableStateFlow<UiState<List<AnimeOverview>>>(UiState.Loading())
    val popularAnimeState: StateFlow<UiState<List<AnimeOverview>>> = _popularAnimeState

    private val _topAiringAnimeState = MutableStateFlow<UiState<List<AnimeOverview>>>(UiState.Loading())
    val topAiringAnimeState: StateFlow<UiState<List<AnimeOverview>>> = _topAiringAnimeState

    private val _mostAnticipatedAnimeState = MutableStateFlow<UiState<List<AnimeOverview>>>(UiState.Loading())
    val mostAnticipatedAnimeState: StateFlow<UiState<List<AnimeOverview>>> = _mostAnticipatedAnimeState

    init {
        getPopularAnime()
        getTopAiringAnime()
        getMostAnticipatedAnime()
    }

    fun getPopularAnime() {
        getPopularAnimeUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    // Use UiState.Loading data class constructor
                    _popularAnimeState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    // Use UiState.Success data class constructor
                    _popularAnimeState.value = UiState.Success(result.data)
                }
                is Resource.Error -> {
                    // Use UiState.Error data class constructor
                    _popularAnimeState.value = UiState.Error(
                        message = result.message ?: "An unexpected error occurred for Popular Anime.",
                        data = result.data,
                        // Determine isRetrying based on your retry logic (e.g., message content)
                        isRetrying = (result.message?.contains("Rate limit exceeded") == true)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getTopAiringAnime() {
        getTopAiringAnimeUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _topAiringAnimeState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _topAiringAnimeState.value = UiState.Success(result.data)
                }
                is Resource.Error -> {
                    _topAiringAnimeState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        isRetrying = (result.message?.contains("Rate limit exceeded") == true)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getMostAnticipatedAnime() {
        getMostAnticipatedAnimeUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _mostAnticipatedAnimeState.value = UiState.Loading()
                }
                is Resource.Success -> {
                    _mostAnticipatedAnimeState.value = UiState.Success(result.data)
                }
                is Resource.Error -> {
                    _mostAnticipatedAnimeState.value = UiState.Error(
                        message = result.message,
                        data = result.data,
                        isRetrying = (result.message.contains("Rate limit exceeded"))
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
