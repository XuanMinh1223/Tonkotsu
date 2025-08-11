// core/data/src/main/java/com/nightfire/tonkotsu.core.data.remote/JikanApi.kt
package com.nightfire.tonkotsu.core.data.remote.api

import com.nightfire.tonkotsu.core.data.remote.dto.CharactersResponse
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeDetailResponse
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeEpisodeDetailResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeEpisodesResponse
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeNewsResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeReviewsResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeSearchResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeSummaryDto
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeVideosResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.ImagesResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.RecommendationResponseDto
import com.nightfire.tonkotsu.core.data.remote.dto.TopAnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    /**
     * Fetches the full details of a specific anime from the Jikan API.
     *
     * @param malId The MyAnimeList ID of the anime.
     * @return A [Response] containing an [AnimeDetailResponse] DTO with the anime's full details.
     */
    @GET("anime/{id}/full")
    suspend fun getAnimeDetail(
        @Path("id") malId: Int
    ): Response<AnimeDetailResponse>

    /**
     * Fetches the episodes of a specific anime from the Jikan API.
     *
     * @param malId The MyAnimeList ID of the anime.
     * @return A [Response] containing an [AnimeEpisodesResponse] DTO with a list of anime episodes.
     */
    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodes(
        @Path("id") malId: Int,
        @Query("page") page: Int? = 1
    ): Response<AnimeEpisodesResponse>

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") malId: Int
    ): Response<CharactersResponse>

    @GET("anime/{id}/pictures")
    suspend fun getAnimeImages(
        @Path("id") malId: Int
    ): Response<ImagesResponseDto>

    @GET("anime/{id}/videos")
    suspend fun getAnimeVideos(
        @Path("id") malId: Int
    ): Response<AnimeVideosResponseDto>

    @GET("anime/{id}/reviews")
    suspend fun getAnimeReviews(
        @Path("id") malId: Int,
        @Query("page") page: Int? = 1,
        @Query("preliminary") preliminary: Boolean? = true,
        @Query("spoiler") spoiler: Boolean? = true
    ): Response<AnimeReviewsResponseDto>

    @GET("anime/{id}/recommendations")
    suspend fun getAnimeRecommendations(
        @Path("id") animeId: Int
    ): Response<RecommendationResponseDto>

    @GET("anime/{id}/episodes/{episode}")
    suspend fun getAnimeEpisodeDetail(
        @Path("id") malId: Int,
        @Path("episode") episodeId: Int
    ): Response<AnimeEpisodeDetailResponseDto>

    @GET("anime/{id}/news")
    suspend fun getAnimeNews(
        @Path("id") malId: Int,
        @Query("page") page: Int? = 1,
        ): Response<AnimeNewsResponseDto>

    @GET("v4/anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("type") type: String? = null, // tv, movie, ova, etc.
        @Query("score") score: Double? = null, // minimum score
        @Query("min_score") minScore: Double? = null,
        @Query("max_score") maxScore: Double? = null,
        @Query("status") status: String? = null, // airing, complete, upcoming
        @Query("rating") rating: String? = null, // g, pg, pg13, r17, r, rx
        @Query("sfw") sfw: Boolean? = null,
        @Query("genres") genres: String? = null, // comma-separated IDs
        @Query("genres_exclude") genresExclude: String? = null,
        @Query("order_by") orderBy: String? = null, // title, score, rank, popularity, etc.
        @Query("sort") sort: String? = null, // asc, desc
        @Query("letter") letter: String? = null,
        @Query("producers") producers: String? = null, // comma-separated IDs
        @Query("start_date") startDate: String? = null, // YYYY-MM-DD
        @Query("end_date") endDate: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null
    ): AnimeSearchResponseDto

}