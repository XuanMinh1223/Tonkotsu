package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun DatePickerRow(
    label: String,
    year: Int?,
    month: Int?,
    day: Int?,
    onYearChange: (Int?) -> Unit,
    onMonthChange: (Int?) -> Unit,
    onDayChange: (Int?) -> Unit
) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text =label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Year dropdown
            DropdownSelector(
                modifier = Modifier.weight(1f),
                label = "Year",
                options = (1990..LocalDate.now().year).toList().toTypedArray(),
                selectedOption = year,
                optionLabel = { it.toString() },
                onSelect = onYearChange,
            )

            // Month dropdown
            DropdownSelector(
                modifier = Modifier.weight(1f),
                label = "Month",
                options = (1..12).toList().toTypedArray(),
                selectedOption = month,
                optionLabel = { it.toString() },
                onSelect = onMonthChange,
            )

            // Day dropdown
            val maxDay = if (year != null && month != null) {
                YearMonth.of(year, month).lengthOfMonth()
            } else 31
            DropdownSelector(
                modifier = Modifier.weight(1f),
                label = "Day",
                options = (1..maxDay).toList().toTypedArray(),
                selectedOption = day,
                optionLabel = { it.toString() },
                onSelect = onDayChange,
            )
        }
    }
}

