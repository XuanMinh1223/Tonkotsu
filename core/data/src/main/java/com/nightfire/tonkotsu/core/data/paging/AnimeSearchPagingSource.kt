package com.nightfire.tonkotsu.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeOverview
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.core.domain.model.AnimeSearchQuery
import com.nightfire.tonkotsu.core.domain.model.toQueryMap

class AnimeSearchPagingSource(
    private val api: JikanApi,
    private val query: AnimeSearchQuery
) : PagingSource<Int, AnimeOverview>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeOverview> {
        val page = params.key ?: 1
        return try {
            val queryMap = query.toQueryMap().toMutableMap()
            queryMap["page"] = page
            queryMap["limit"] = params.loadSize.coerceAtMost(25)

            val response = api.animeSearch(queryMap)
            val anime = response.data.map { it.toAnimeOverview() }

            val nextKey = if (anime.isEmpty()) null else page + 1

            LoadResult.Page(
                data = anime,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeOverview>): Int? {
        // Try to find the page key closest to the anchor position
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
