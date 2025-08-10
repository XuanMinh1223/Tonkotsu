package com.nightfire.tonkotsu.ui.fullscreenoverlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun ReviewItemFullScreen(
    content: OverlayContent.ReviewFullScreen, // Changed parameter type
    modifier: Modifier = Modifier,
) {
    val review = content.review
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }

    Column(
        modifier = modifier
            .fillMaxSize() // Fill the pager page
            .verticalScroll(rememberScrollState()) // Make content scrollable
            .padding(16.dp) // Padding inside the full screen item
    ) {
        // User Info Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(review.user.avatarUrl) // Use imageUrl from ReviewUser
                    .crossfade(true)
                    .build(),
                contentDescription = "${review.user.username}'s avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp) // Slightly larger avatar for full screen
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.user.username, // Handle null user
                    style = MaterialTheme.typography.titleLarge, // Larger text for full screen
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val reviewMeta = buildAnnotatedString {
                    review.date?.let { append(it.format(dateFormatter)) }
                     review.episodesWatched?.let {
                         if (review.date != null) append(" • ")
                         append("$it eps watched")
                     }
                }
                if (reviewMeta.isNotEmpty()) {
                    Text(
                        text = reviewMeta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            review.score?.let { score ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Score",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp) // Larger star icon
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.headlineSmall, // Larger score text
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
         if (review.isSpoiler || review.isPreliminary) {
             Spacer(Modifier.height(12.dp))
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