package com.nightfire.tonkotsu.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.Resource
import com.nightfire.tonkotsu.core.common.UiState // Import your new UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.usecase.GetTopAnimeOverviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopAnimeOverviewUseCase: GetTopAnimeOverviewUseCase
) : ViewModel() {

    private val _popularAnimeState = MutableStateFlow(UiState<List<AnimeOverview>>())
    val popularAnimeState: StateFlow<UiState<List<AnimeOverview>>> = _popularAnimeState

    init {
        getTopAnimeOverview()
    }

    private fun getTopAnimeOverview() {
        getTopAnimeOverviewUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _popularAnimeState.value = UiState.loading(result.data)
                }
                is Resource.Success -> {
                    _popularAnimeState.value = UiState.success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _popularAnimeState.value = UiState.error(
                        message = result.message ?: "An unexpected error occurred",
                        data = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}