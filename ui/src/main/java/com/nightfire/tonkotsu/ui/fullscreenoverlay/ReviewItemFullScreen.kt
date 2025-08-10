package com.nightfire.tonkotsu.ui.fullscreenoverlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ReviewItemFullScreen(
    content: OverlayContent.ReviewFullScreen, // Changed parameter type
    modifier: Modifier = Modifier,
) {
    val review = content.review

    Column(
        modifier = modifier
            .fillMaxSize() // Fill the pager page
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp) // Padding inside the full screen item
    ) {
         if (review.isSpoiler || review.isPreliminary) {
             Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                 if (review.isSpoiler) {
                     AssistChip(
                         onClick = { /* No action or toggle blur */ },
                         label = { Text("Spoiler", style = MaterialTheme.typography.labelMedium) },
                         leadingIcon = {
                             Icon(
                                 Icons.Filled.Check,
                                 contentDescription = "Spoiler warning",
                                 modifier = Modifier.size(AssistChipDefaults.IconSize)
                             )
                         },
                         colors = AssistChipDefaults.assistChipColors(
                             labelColor = MaterialTheme.colorScheme.onErrorContainer,
                             leadingIconContentColor = MaterialTheme.colorScheme.onErrorContainer,
                             containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                         ),
                         border = null
                     )
                 }
                 if (review.isPreliminary) {
                     AssistChip(
                         onClick = { /* No action */ },
                         label = { Text("Preliminary", style = MaterialTheme.typography.labelMedium) },
                         leadingIcon = {
                             Icon(
                                 Icons.Filled.Info,
                                 contentDescription = "Preliminary review",
                                 modifier = Modifier.size(AssistChipDefaults.IconSize)
                             )
                         },
                         colors = AssistChipDefaults.assistChipColors(
                             labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                             leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                             containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                         ),
                         border = null
                     )
                 }
             }
         }

        Spacer(Modifier.height(20.dp))

        // Review Content (fully scrollable within this item)
        review.reviewContent?.let { content -> // Use review.content from your Review model
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}