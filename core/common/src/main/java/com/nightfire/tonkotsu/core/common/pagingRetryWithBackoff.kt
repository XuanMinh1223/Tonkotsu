package com.nightfire.tonkotsu.core.common

import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> pagingRetryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentAttempt = 0
    var currentDelay = initialDelayMillis

    while (true) {
        try {
            return block()
        } catch (e: Exception) {
            val shouldRetry = when (e) {
                is HttpException -> e.code() == 429
                is IOException -> true
                else -> false
            }
            if (!shouldRetry || currentAttempt >= maxRetries) throw e

            delay(currentDelay)
            currentAttempt++
            currentDelay = (currentDelay * factor).toLong()
        }
    }
}
