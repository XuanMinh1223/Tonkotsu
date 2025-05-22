package com.nightfire.tonkotsu.core.data.repository


import com.nightfire.tonkotsu.core.common.Resource // Import the Resource sealed class
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeDto // Import the DTO
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview // Import your domain model
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository // Import your repository interface
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject // Hilt annotation for constructor injection

/**
 * Implementation of the [AnimeRepository] interface.
 * This class handles fetching anime data from the Jikan API and mapping it to domain models.
 * It also wraps the results in a [Resource] sealed class for state management.
 */
class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApi
) : AnimeRepository {

    override suspend fun getTopAnimeOverview(
        type: String?,
        filter: String?,
        page: Int?,
        limit: Int?
    ): Resource<List<AnimeOverview>> {
        return try {

            val response = api.getTopAnime(
                type = type,
                filter = filter,
                page = page,
                limit = limit
            )

            // Map the list of AnimeDto to a list of AnimeOverview domain models
            val domainList = response.data.map { it.toAnimeOverview() }

            // Return success with the mapped domain models
            Resource.Success(domainList)

        } catch (e: HttpException) {
            Resource.Error(
                message = e.localizedMessage ?: "An unexpected HTTP error occurred",
                data = null // No data available on HTTP error
            )
        } catch (e: IOException) {
            Resource.Error(
                message = "Couldn't reach server. Check your internet connection.",
                data = null // No data available on network error
            )
        } catch (e: Exception) {
            Resource.Error(
                message = e.localizedMessage ?: "An unknown error occurred",
                data = null // No data available on unknown error
            )
        }
    }
}

/**
 * Extension function to map an [AnimeDto] to an [AnimeOverview] domain model.
 * This keeps the mapping logic isolated and clean.
 */
fun AnimeDto.toAnimeOverview(): AnimeOverview {
    return AnimeOverview(
        malId = malId,
        title = title,
        imageUrl = images?.jpg?.imageUrl,
        score = score,
        type = type,
        episodes = episodes,
        synopsis = synopsis
    )
}