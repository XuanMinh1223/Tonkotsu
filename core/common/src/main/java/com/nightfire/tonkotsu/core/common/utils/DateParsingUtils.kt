package com.nightfire.tonkotsu.core.common.utils

import android.util.Log
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun parseDateString(dateString: String?): LocalDate? {
    return dateString?.let {
        try {
            // Attempt to parse as a full ISO OffsetDateTime
            OffsetDateTime.parse(it).toLocalDate()
        } catch (e: DateTimeParseException) {
            // If that fails, try parsing as a simple LocalDate (e.g., "YYYY-MM-DD")
            try {
                LocalDate.parse(it)
            } catch (e: DateTimeParseException) {
                // If all specific parsing attempts fail, try as ISO_LOCAL_DATE_TIME (without offset)
                try {
                    LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                } catch (e: DateTimeParseException) {
                    Log.e("AnimeDetailMapper", "Failed to parse date: $it", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("AnimeDetailMapper", "An unexpected error occurred parsing date: $it", e)
            null
        }
    }
}