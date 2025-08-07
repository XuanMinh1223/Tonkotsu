package com.nightfire.tonkotsu.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode

class AnimeEpisodesPagingSource(
    private val api: JikanApi,
    private val animeId: Int
) : PagingSource<Int, AnimeEpisode>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeEpisode> {
        val page = params.key ?: 1
        return try {
            val response = api.getAnimeEpisodes(animeId, page)
            val episodes = response.body()?.data?.run {
                this.map { it.toAnimeEpisode() }
            } ?: emptyList()
            LoadResult.Page(
                data = episodes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (episodes.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeEpisode>): Int? {
        return state.anchorPosition
    }
}
