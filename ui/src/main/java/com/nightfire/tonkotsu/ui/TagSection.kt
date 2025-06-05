package com.nightfire.tonkotsu.ui // Your ui module package

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class) // FlowRow is an experimental API
@Composable
fun TagSection(
    title: String,
    tags: List<String>?,
    onTagClick: (String) -> Unit = {},
    isSecondary: Boolean = false
) {
    val contentColor = if (isSecondary) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer
    val containerColor = if (isSecondary) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    tags?.takeIf { it.isNotEmpty() }?.let { tagList ->
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tagList.forEach { tag ->
                Text(
                    text = tag,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    modifier = Modifier
                        .background(
                            containerColor,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable { onTagClick(tag) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}