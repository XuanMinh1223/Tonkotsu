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

fun parseDateTimeString(dateTimeString: String?): OffsetDateTime? {
    return dateTimeString?.let {
        try {
            // Attempt to parse directly as ISO_OFFSET_DATE_TIME
            // This is the most common format for date-times with offsets (e.g., "2007-12-03T10:15:30+01:00")
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: DateTimeParseException) {
            // If that fails, try with a more general ISO_DATE_TIME.
            // This might handle cases where the offset is 'Z' or potentially other variations,
            // though OffsetDateTime.parse(String) is often robust enough.
            try {
                OffsetDateTime.parse(it) // This is quite flexible and handles many ISO 8601 formats
            } catch (e2: DateTimeParseException) {
                // Log the failure if all attempts fail
                Log.w("DateParsingUtils", "Failed to parse date-time string to OffsetDateTime: $it", e2)
                null
            }
        } catch (e: Exception) {
            // Catch any other unexpected errors during parsing
            Log.e("DateParsingUtils", "An unexpected error occurred parsing date-time string: $it", e)
            null
        }
    }
}