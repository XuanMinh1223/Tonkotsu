package com.nightfire.tonkotsu.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.toAnimeReview
import com.nightfire.tonkotsu.core.domain.model.AnimeReview

class AnimeReviewsPagingSource(
    private val api: JikanApi,
    private val animeId: Int
) : PagingSource<Int, AnimeReview>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeReview> {
        val page = params.key ?: 1
        return try {
            val response = api.getAnimeReviews(animeId, page)
            val responseBody = response.body()
            val episodes = responseBody?.data?.map { it.toAnimeReview() } ?: emptyList()
            val hasNextPage = responseBody?.pagination?.hasNextPage == true

            LoadResult.Page(
                data = episodes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (hasNextPage) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AnimeReview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}