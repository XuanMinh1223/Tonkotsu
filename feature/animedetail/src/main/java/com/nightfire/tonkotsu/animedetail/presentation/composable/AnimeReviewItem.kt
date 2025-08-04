package com.nightfire.tonkotsu.ui.composables

// ... (existing imports, including Chip if you use it, or Badge)
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun AnimeReviewItem(
    review: AnimeReview,
    modifier: Modifier = Modifier,
    itemWidth: Dp = 300.dp // Define a default width for items in a row
) {
    val context = LocalContext.current
    val maxLines = 4 // Max lines for review content when collapsed
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }

    Card(
        modifier = modifier
            .width(itemWidth) // <--- Apply a fixed width
            .padding(vertical = 6.dp), // Removed horizontal padding here, handle in LazyRow's arrangement
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding inside the card
                .fillMaxHeight() // Allow content to use the card's height
        ) {
            // User Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // Row can fill the card's width
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(review.user.avatarUrl) // The URL of the avatar from your ReviewUser model
                        .crossfade(true) // Enable crossfade animation
                        .build(),
                    contentDescription = "${review.user.username}'s avatar",
                    contentScale = ContentScale.Crop, // Crop to fit the bounds, good for circles
                    modifier = Modifier
                        .size(40.dp) // Size of the avatar
                        .clip(CircleShape) // Clip to a circle
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)) // Placeholder background
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.user.username,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1, // Ensure username doesn't wrap excessively
                        overflow = TextOverflow.Ellipsis
                    )
                    // Combine date and episodes watched if available
                    val reviewMeta = buildAnnotatedString {
                        review.date?.let { append(it.format(dateFormatter)) } // Simplified for row
                        review.episodesWatched?.let {
                            if (review.date != null) append(" • ")
                            append("$it eps") // Shorten "episodes watched"
                        }
                    }
                    if (reviewMeta.isNotEmpty()) {
                        Text(
                            text = reviewMeta,
                            style = MaterialTheme.typography.bodySmall,
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
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "$score", // Just the score for brevity
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Spoiler, Preliminary Chips/Badges
            if (review.isSpoiler || review.isPreliminary) {
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp), // Reduce spacing for chips
                    modifier = Modifier.fillMaxWidth() // Allow chips to align if space allows
                ) {
                    if (review.isSpoiler) {
                        AssistChip(
                            onClick = { /* No action or toggle blur */ },
                            label = { Text("Spoiler", style = MaterialTheme.typography.labelSmall) }, // Smaller text
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Spoiler warning",
                                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            // ... (rest of chip properties, consider smaller padding/size)
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = MaterialTheme.colorScheme.onErrorContainer,
                                leadingIconContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                            ),
                            border = null,
                            modifier = Modifier.height(28.dp) // Adjust chip height
                        )
                    }
                    if (review.isPreliminary) {
                        AssistChip(
                            onClick = { /* No action */ },
                            label = { Text("Early", style = MaterialTheme.typography.labelSmall) }, // "Preliminary" -> "Early"
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Info,
                                    contentDescription = "Preliminary review",
                                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                            // ... (rest of chip properties)
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                            ),
                            border = null,
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Review Content
            review.reviewContent?.let { content ->
                val reviewText = if (review.isSpoiler) {
                    "This review contains spoilers. Tap 'Show More' to read."
                } else {
                    content
                }
                Text(
                    text = reviewText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLines, // Limit max lines even when expanded in a row
                    overflow = TextOverflow.Ellipsis, // Always ellipsis if too long for the given lines
                )

                val actualLines = content.lines().size // Basic line count
                val canShowMore = actualLines > maxLines || content.length > maxLines * 50 // Heuristic for row items

            }
            // Add reactions or other elements if needed, keeping horizontal space in mind
        }
    }
}