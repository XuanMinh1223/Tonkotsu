package com.nightfire.tonkotsu.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nightfire.tonkotsu.core.common.pagingRetryWithBackoff
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.toNews
import com.nightfire.tonkotsu.core.domain.model.News
import retrofit2.HttpException
import java.io.IOException

class AnimeNewsPagingSource(
    private val api: JikanApi,
    private val animeId: Int
) : PagingSource<Int, News>() {

    companion object {
        private const val MAX_RETRIES = 3
        private fun calculateBackoffDelay(attempt: Int): Long =
            (1000L * attempt * attempt) // exponential-ish backoff in ms

        private fun isRateLimitOrNetworkError(e: Throwable): Boolean {
            return (e is HttpException && e.code() == 429) || e is IOException
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: 1

        return try {
            pagingRetryWithBackoff {
                val response = api.getAnimeNews(animeId, page)
                if (response.isSuccessful) {
                    val body = response.body()
                    val newsList = body?.data?.map { it.toNews() } ?: emptyList()
                    val hasNextPage = body?.pagination?.hasNextPage == true

                    LoadResult.Page(
                        data = newsList,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (hasNextPage) page + 1 else null
                    )
                } else {
                    throw HttpException(response)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
