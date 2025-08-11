package com.nightfire.tonkotsu.feature.search

import android.app.appsearch.SearchResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nightfire.tonkotsu.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    data class SearchScreenState(
        val query: String = "",
    )

    private val _screenState = MutableStateFlow(SearchScreenState())
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    private val _results = MutableStateFlow<UiState<List<SearchResult>>>(UiState.Success(emptyList()))
    val results: StateFlow<UiState<List<SearchResult>>> = _results.asStateFlow()

    fun onQueryChange(query: String) {
        _screenState.update { it.copy(query = query) }
        search()
    }

    private fun search() {
        val state = _screenState.value
        if (state.query.isBlank()) {
            _results.value = UiState.Success(emptyList())
            return
        }

        viewModelScope.launch {
            _results.value = UiState.Loading(
                data = (_results.value as? UiState.Success)?.data
            )
        }
    }
}