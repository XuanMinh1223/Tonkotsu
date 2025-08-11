package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline, // Or onBackground.copy(alpha=0.1f), etc.
        modifier = modifier.padding(vertical = 16.dp) // Apply default padding here
    )
}