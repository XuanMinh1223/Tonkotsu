package com.nightfire.tonkotsu.ui.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardRowSkeleton(
    modifier: Modifier = Modifier,
    itemsToShow: Int = 10 // Number of skeleton cards to display (enough to fill screen and scroll slightly)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Match spacing of your actual LazyRow
        contentPadding = PaddingValues(horizontal = 8.dp) // Match content padding
    ) {
        items(itemsToShow) {
            CardSkeleton() // Use the skeleton card composable for each item
        }
    }
}