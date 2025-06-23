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

    var currentAttempt = 0 // Track attempts within the flow
    while (currentAttempt <= RetryConfig.MAX_RETRIES) { // Loop for retries
        try {
            val response = apiCall() // Execute the actual API call

            if (response.isSuccessful) {
                val dtoBody = response.body()
                if (dtoBody != null) {
                    val data = mapper(dtoBody)
                    emit(Resource.Success(data))
                    break // Success, exit loop
                } else {
                    emit(Resource.Error("API returned a successful response with a null body."))
                    break // Non-retryable error, exit loop
                }
            } else {
                val httpException = HttpException(response)
                val errorMessage = when (httpException.code()) {
                    404 -> "Item not found. Please check the ID."
                    429 -> {
                        val retryAfterSeconds = httpException.response()?.headers()?.get("Retry-After")?.toLongOrNull()
                        if (retryAfterSeconds != null && retryAfterSeconds > 0) {
                            "Rate limit exceeded. Please wait for ${retryAfterSeconds} seconds before retrying."
                        } else {
                            "Rate limit exceeded. Trying again with exponential backoff."
                        }
                    }
                    in 400..499 -> "Client error ${httpException.code()}: ${httpException.message()}."
                    in 500..599 -> "Server error ${httpException.code()}: Please try again later."
                    else -> httpException.localizedMessage ?: "An unexpected HTTP error occurred."
                }

                val willRetry = currentAttempt < RetryConfig.MAX_RETRIES && isRateLimitOrNetworkError(httpException)
                emit(Resource.Error(errorMessage, isRetrying = willRetry)) // Pass isRetrying flag

                if (willRetry) {
                    val delayMillis = calculateBackoffDelay(currentAttempt + 1)
                    println("Retrying attempt ${currentAttempt + 1} in ${delayMillis}ms due to: ${httpException.message}")
                    delay(delayMillis)
                    currentAttempt++ // Increment attempt for next iteration
                    emit(Resource.Loading) // Re-emit loading before the next attempt
                } else {
                    break // No more retries, exit loop
                }
            }
        } catch (e: IOException) {
            val willRetry = currentAttempt < RetryConfig.MAX_RETRIES && isRateLimitOrNetworkError(e)
            emit(Resource.Error(e.localizedMessage ?: "Couldn't reach server. Check your internet connection.", isRetrying = willRetry))
            if (willRetry) {
                val delayMillis = calculateBackoffDelay(currentAttempt + 1)
                println("Retrying attempt ${currentAttempt + 1} in ${delayMillis}ms due to: ${e.message}")
                delay(delayMillis)
                currentAttempt++
                emit(Resource.Loading)
            } else {
                break
            }
        } catch (e: Exception) {
            // General exception, likely not retryable unless specifically handled
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred.", isRetrying = false))
            break
        }
    }
}