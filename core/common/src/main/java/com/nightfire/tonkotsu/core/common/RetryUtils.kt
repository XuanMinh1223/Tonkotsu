package com.nightfire.tonkotsu.core.common

import retrofit2.HttpException
import java.io.IOException
import kotlin.math.pow

object RetryConfig {
    const val MAX_RETRIES = 3
    const val INITIAL_BACKOFF_MILLIS = 1000L
    const val BACKOFF_FACTOR = 2
}

fun isRateLimitOrNetworkError(cause: Throwable): Boolean {
    return when (cause) {
        is HttpException -> cause.code() == 429
        is IOException -> true
        else -> false
    }
}

fun calculateBackoffDelay(attempt: Long): Long {
    val exponent = (attempt - 1).toInt()
    return (RetryConfig.INITIAL_BACKOFF_MILLIS * RetryConfig.BACKOFF_FACTOR.toDouble().pow(exponent)).toLong()
}