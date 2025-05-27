// core/data/src/main/java/com/nightfire/tonkotsu.core.data.remote/JikanApi.kt
package com.nightfire.tonkotsu.core.data.remote.api

import com.nightfire.tonkotsu.core.data.remote.dto.TopAnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API Service interface for interacting with the Jikan API.
 * Defines the HTTP endpoints and their expected request/response types.
 */
interface JikanApi {

    /**
     * Fetches a list of top anime from the Jikan API.
     *
     * @param type (Optional) Filter by anime type (e.g., "tv", "movie", "ova").
     * @param filter (Optional) Filter by status (e.g., "airing", "upcoming", "bypopularity").
     * @param page (Optional) The page number for pagination.
     * @param limit (Optional) The number of results per page.
     * @return A [TopAnimeResponse] DTO containing a list of [AnimeDto] and pagination info.
     */
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("type") type: String? = null,
        @Query("filter") filter: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sfw") sfw: Boolean = true
    ): Response<TopAnimeResponse>
}