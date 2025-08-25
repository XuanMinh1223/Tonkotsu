package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nightfire.tonkotsu.core.domain.model.AnimeFilterOptions

@Composable
fun ScoreRangeFilter(
    initialRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    var sliderRange by remember { mutableStateOf(initialRange) }

    Column {
        Text(
            text = "Score: ${"%.1f".format(sliderRange.start)} - ${"%.1f".format(sliderRange.endInclusive)}",
            style = MaterialTheme.typography.bodyMedium
        )

        RangeSlider(
            value = sliderRange,
            onValueChange = { newRange ->
                sliderRange = newRange
            },
            valueRange = 0f..10f,
            steps = 0
        )
    }

    // Expose the current selection upward (but don't apply yet)
    LaunchedEffect(sliderRange) {
        onRangeChange(sliderRange)
    }
}

