package com.nightfire.tonkotsu.core.data.repository


import android.util.Log
import com.nightfire.tonkotsu.core.common.Resource // Import the Resource sealed class
import com.nightfire.tonkotsu.core.data.Constants.RetryConfig
import com.nightfire.tonkotsu.core.data.remote.api.JikanApi
import com.nightfire.tonkotsu.core.data.remote.dto.AnimeDto // Import the DTO
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview // Import your domain model
import com.nightfire.tonkotsu.core.domain.repository.AnimeRepository // Import your repository interface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject // Hilt annotation for constructor injection
import kotlinx.coroutines.flow.retryWhen
/**
 * Implementation of the [AnimeRepository] interface.
 * This class handles fetching anime data from the Jikan API and mapping it to domain models.
 * It also wraps the results in a [Resource] sealed class for state management.
 */
class AnimeRepositoryImpl @Inject constructor(
    private val api: JikanApi
) : AnimeRepository {

    override fun getTopAnimeOverview(
        type: String?,
        filter: String?,
        page: Int?,
        limit: Int?
    ): Flow<Resource<List<AnimeOverview>>> = flow {
        try {
            emit(Resource.Loading)

            val response = api.getTopAnime( // This is your suspend Retrofit call
                type = type,
                filter = filter,
                page = page,
                limit = limit
            )

            if (response.isSuccessful) {
                val domainList = response.body()?.data?.map { it.toAnimeOverview() }
                    ?: emptyList()
                emit(Resource.Success(domainList))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "An unexpected HTTP error occurred"
                emit(Resource.Error(
                    message = errorMessage,
                    data = null
                ))
                throw HttpException(response)
            }

        } catch (e: HttpException) {
            val errorMessage = e.localizedMessage ?: "An HTTP error occurred"
            Log.e("HTTP Exception in getTopAnimeOverview: %s", errorMessage)
            emit(Resource.Error(
                message = errorMessage,
                data = null
            ))
            throw e
        } catch (e: IOException) {
            val errorMessage = "Couldn't reach server. Check your internet connection."
            Log.e( "Network Exception in getTopAnimeOverview: %s", errorMessage)
            emit(Resource.Error(
                message = errorMessage,
                data = null
            ))
            throw e
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "An unknown error occurred"
            Log.e( "Unexpected Exception in getTopAnimeOverview: %s", errorMessage)
            emit(Resource.Error(
                message = errorMessage,
                data = null
            ))
            // Re-throw the exception to propagate it
            throw e
        }
    }.retryWhen { cause, attempt ->
        if (attempt < RetryConfig.MAX_RETRIES && isRateLimitOrNetworkError(cause)) {
            val delayMillis = calculateBackoffDelay(attempt)
            delay(delayMillis) // Suspend and wait for the calculated delay
            true
        } else {
            false
        }
    }

    private fun isRateLimitOrNetworkError(cause: Throwable): Boolean {
        return when (cause) {
            is HttpException -> cause.code() == 429 // Check for HTTP 429 (Too Many Requests)
            is IOException -> true // Network connectivity issues are often transient
            else -> false // Don't retry for other types of exceptions (e.g., parsing errors, logic errors)
        }
    }

    // Helper function to calculate exponential backoff with jitter
    private fun calculateBackoffDelay(attempt: Long): Long {
        // Base delay grows exponentially: initial * 2^attempt
        var delay = RetryConfig.INITIAL_RETRY_DELAY_MS * (1L shl attempt.toInt()) // 1L shl attempt = 2^attempt

        // Cap the delay at the maximum
        if (delay > RetryConfig.MAX_RETRY_DELAY_MS) {
            delay = RetryConfig.MAX_RETRY_DELAY_MS
        }

        // Add jitter: +/- 25% of the calculated delay
        val jitterRange = delay / 4 // 25% of the delay
        // Generate a random value within [-jitterRange, +jitterRange]
        val randomJitter = ((-jitterRange)..jitterRange).random()
        return delay + randomJitter
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