package com.nightfire.tonkotsu.core.data

class Constants {
    object RetryConfig {
        const val INITIAL_RETRY_DELAY_MS = 500L // 0.5 seconds
        const val MAX_RETRY_DELAY_MS = 60_000L // 60 seconds (1 minute)
        const val MAX_RETRIES = 5 // Max number of retries before giving up
    }
}