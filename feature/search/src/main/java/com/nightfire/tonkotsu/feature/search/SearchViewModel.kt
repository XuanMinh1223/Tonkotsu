package com.nightfire.tonkotsu.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nightfire.tonkotsu.core.domain.model.AnimeFilterOptions
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.model.AnimeSearchQuery
import com.nightfire.tonkotsu.core.domain.usecase.AnimeSearchUseCase
import com.nightfire.tonkotsu.core.domain.model.AnimeOrderBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    animeSearchUseCase: AnimeSearchUseCase
): ViewModel() {
    private val _searchQuery = MutableStateFlow(
        AnimeSearchQuery(
            orderBy = AnimeOrderBy.POPULARITY.apiName,
            sfw = true
        )
    )
    val searchQuery: StateFlow<AnimeSearchQuery> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: StateFlow<PagingData<AnimeOverview>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .filter { query -> !query.query.isNullOrBlank() }  // Filter out empty or null queries
        .flatMapLatest { query ->
            animeSearchUseCase(query)
                .cachedIn(viewModelScope)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun updateSearchQuery(newQuery: AnimeSearchQuery) {
        _searchQuery.value = newQuery
    }

    fun updateSearchFilter(filter: AnimeFilterOptions) {
        _searchQuery.value = _searchQuery.value.copy(
            startDate = filter.startDate,
            endDate = filter.endDate,
            type = filter.type.apiValue,
            status = filter.status?.apiValue,
            rating = filter.rating?.apiValue,
            minScore = filter.minScore?.toDouble(),
            maxScore = filter.maxScore?.toDouble(),
        )
    }
}