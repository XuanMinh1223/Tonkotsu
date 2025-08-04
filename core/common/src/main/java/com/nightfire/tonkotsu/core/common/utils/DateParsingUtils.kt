package com.nightfire.tonkotsu.core.common.utils

import android.util.Log
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun parseDateTimeString(dateTimeString: String?): OffsetDateTime? {
    return dateTimeString?.let {
        try {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        } catch (e: DateTimeParseException) {
            try {
                OffsetDateTime.parse(it)
            } catch (e2: DateTimeParseException) {
                Log.w("DateParsingUtils", "Failed to parse date-time string to OffsetDateTime: $it", e2)
                null
            }
        } catch (e: Exception) {
            Log.e("DateParsingUtils", "An unexpected error occurred parsing date-time string: $it", e)
            null
        }
    }
}