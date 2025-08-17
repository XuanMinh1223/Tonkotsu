package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeOrderBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSheet(
    sheetState: SheetState,
    currentOrderBy: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Sort by", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            // Iterate through enums instead of hardcoded list
            AnimeOrderBy.entries.forEach { orderBy ->
                ListItem(
                    headlineContent = {
                        Text(
                            orderBy.label,
                            style = if (orderBy.apiName == currentOrderBy)
                                MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            else
                                MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier.clickable {
                        onSelect(orderBy.apiName)
                    }
                )
            }
        }
    }
}

