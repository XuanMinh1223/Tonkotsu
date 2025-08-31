package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeFilterOptions
import com.nightfire.tonkotsu.core.domain.util.AnimeRating
import com.nightfire.tonkotsu.core.domain.util.AnimeStatus
import com.nightfire.tonkotsu.core.domain.util.AnimeType
import com.nightfire.tonkotsu.ui.DatePickerRow
import com.nightfire.tonkotsu.ui.DropdownSelector
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    sheetState: SheetState,
    currentOptions: AnimeFilterOptions,
    onApply: (AnimeFilterOptions) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Local state to hold temporary edits
    var type by remember(currentOptions) { mutableStateOf(currentOptions.type) }
    var status by remember(currentOptions) { mutableStateOf(currentOptions.status) }
    var rating by remember(currentOptions) { mutableStateOf(currentOptions.rating) }
    var scoreRange by remember(currentOptions) {
        mutableStateOf((currentOptions.minScore ?: 0f)..(currentOptions.maxScore ?: 10f))
    }

    // Parse current start/end dates to parts
    var startYear by remember(currentOptions) { mutableStateOf(currentOptions.startDate?.take(4)?.toIntOrNull()) }
    var startMonth by remember(currentOptions) { mutableStateOf(currentOptions.startDate?.drop(5)?.take(2)?.toIntOrNull()) }
    var startDay by remember(currentOptions) { mutableStateOf(currentOptions.startDate?.drop(8)?.take(2)?.toIntOrNull()) }

    var endYear by remember(currentOptions) { mutableStateOf(currentOptions.endDate?.take(4)?.toIntOrNull()) }
    var endMonth by remember(currentOptions) { mutableStateOf(currentOptions.endDate?.drop(5)?.take(2)?.toIntOrNull()) }
    var endDay by remember(currentOptions) { mutableStateOf(currentOptions.endDate?.drop(8)?.take(2)?.toIntOrNull()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            // TYPE Dropdown
            DropdownSelector(
                label = "Type",
                options = AnimeType.entries.toTypedArray(),
                selectedOption = type,
                optionLabel = { it.displayName }
            ) { type = it }

            Spacer(Modifier.height(12.dp))

            // SCORE slider
            Text("Score Range", style = MaterialTheme.typography.labelLarge)

            ScoreRangeFilter(
                initialRange = scoreRange,
                onRangeChange = { range -> scoreRange = range }
            )

            Spacer(Modifier.height(12.dp))

            // STATUS Dropdown
            DropdownSelector(
                label = "Status",
                options = AnimeStatus.entries.toTypedArray(),
                selectedOption = status,
                optionLabel = { it.displayName }
            ) { newStatus ->
                status = newStatus
            }

            Spacer(Modifier.height(12.dp))

            // RATING Dropdown
            DropdownSelector(
                label = "Rating",
                options = AnimeRating.entries.toTypedArray(),
                selectedOption = rating,
                optionLabel = { it?.description ?: "All ratings" }
            ) { newRating -> rating = newRating }

            Spacer(Modifier.height(12.dp))

            // DATE PICKERS
            DatePickerRow(
                label = "Start Date",
                year = startYear,
                month = startMonth,
                day = startDay,
                onYearChange = { startYear = it },
                onMonthChange = { startMonth = it },
                onDayChange = { startDay = it }
            )
            DatePickerRow(
                label = "End Date",
                year = endYear,
                month = endMonth,
                day = endDay,
                onYearChange = { endYear = it },
                onMonthChange = { endMonth = it },
                onDayChange = { endDay = it }
            )

            // APPLY BUTTON
            Button(
                onClick = {
                    val formattedStart = buildDateString(startYear, startMonth, startDay)
                    val formattedEnd = buildDateString(endYear, endMonth, endDay)
                    onApply(
                        AnimeFilterOptions(
                            type = type,
                            minScore = scoreRange.start,
                            maxScore = scoreRange.endInclusive,
                            status = status,
                            rating = rating,
                            startDate = formattedStart,
                            endDate = formattedEnd
                        )
                    )
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Apply Filters")
            }
        }
    }
}


// Helper to format partial dates safely
fun buildDateString(year: Int?, month: Int?, day: Int?): String {
    return when {
        year == null -> ""
        month == null -> String.format("%04d-01-01", year) // default Jan 1
        day == null -> String.format("%04d-%02d-01", year, month) // default 1st day of month
        else -> String.format("%04d-%02d-%02d", year, month, day)
    }
}
