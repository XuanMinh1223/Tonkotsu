package com.nightfire.tonkotsu.core.common


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response // Import Retrofit's Response class!
import java.io.IOException

// Make sure RetryConfig, isRateLimitOrNetworkError, calculateBackoffDelay are also accessible

/**
 * A generic helper function to wrap network calls that return Retrofit's Response<DTO>.
 * It handles loading, success, error states, and includes retry logic for
 * rate limiting and network errors.
 *
 * @param apiCall A suspend lambda that performs the actual API call, returning a Response<DTO_TYPE>.
 * @param mapper A lambda that maps the DTO_TYPE (from the successful response body) to your Domain Model type.
 * @return A Flow emitting Resource states (Loading, Success, Error).
 */
fun <DomainModel, DtoType> networkBoundResourceFlow(
    apiCall: suspend () -> Response<DtoType>, // Now expects a Retrofit Response
    mapper: (DtoType) -> DomainModel // Mapper from DTO to DomainModel
): Flow<Resource<DomainModel>> = flow {
    emit(Resource.Loading) // Emit Loading state immediately

    try {
        val response = apiCall() // Execute the actual API call

        if (response.isSuccessful) {
            val dtoBody = response.body()
            if (dtoBody != null) {
                // Map the DTO body to the domain model
                val data = mapper(dtoBody)
                emit(Resource.Success(data))
            } else {
                // If the response was successful but the body was null, treat as an error
                emit(Resource.Error("API returned a successful response with a null body."))
            }
        } else {
            // If the response was not successful (e.g., 4xx, 5xx), throw HttpException
            // This ensures our catch block for HttpException is used for consistent error messages
            throw HttpException(response)
        }
    } catch (e: HttpException) {
        // Handle HTTP errors based on the status code from the HttpException
        val errorMessage = when (e.code()) {
            404 -> "Item not found. Please check the ID."
            429 -> {
                val retryAfterSeconds = e.response()?.headers()?.get("Retry-After")?.toLongOrNull()
                if (retryAfterSeconds != null && retryAfterSeconds > 0) { // Only mention if non-null and positive
                    "Rate limit exceeded. Please wait for ${retryAfterSeconds} seconds before retrying."
                } else {
                    // This message indicates we're falling back to our internal backoff
                    "Rate limit exceeded. Trying again with exponential backoff."
                }
            }
            in 400..499 -> "Client error ${e.code()}: ${e.message()}."
            in 500..599 -> "Server error ${e.code()}: Please try again later."
            else -> e.localizedMessage ?: "An unexpected HTTP error occurred."
        }
        emit(Resource.Error(errorMessage))
    } catch (e: IOException) {
        // Handle network errors (e.g., no internet connection, timeout)
        emit(Resource.Error(e.localizedMessage ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: Exception) {
        // Catch any other unexpected errors
        emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred."))
    }
}.retryWhen { cause, attempt ->
    if (attempt < RetryConfig.MAX_RETRIES && isRateLimitOrNetworkError(cause)) {
        var delayMillis = calculateBackoffDelay(attempt + 1)

        // If it's a 429 HttpException, check for Retry-After header and adjust delay
        if (cause is HttpException && cause.code() == 429) {
            val retryAfterSeconds = cause.response()?.headers()?.get("Retry-After")?.toLongOrNull()
            if (retryAfterSeconds != null) {
                // Wait for at least the Retry-After duration, but don't exceed max backoff
                // Use maxOf to ensure we wait for at least the calculated backoff OR the Retry-After
                delayMillis = maxOf(delayMillis, retryAfterSeconds * 1000L)
            }
        }

        println("Retrying attempt ${attempt + 1} in ${delayMillis}ms due to: ${cause.message}")
        delay(delayMillis)
        true // Re-emit the flow to retry
    } else {
        false // Do not retry
    }
}