package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortBar(
    currentOrderBy: String,
    onOrderByChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // OrderBy selector
        FilledTonalButton(onClick = { onOrderByChange(currentOrderBy) }) {
            Text(currentOrderBy.replaceFirstChar { it.uppercase() })
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Change sort field"
            )
        }

        Spacer(Modifier.weight(1f))
    }
}


