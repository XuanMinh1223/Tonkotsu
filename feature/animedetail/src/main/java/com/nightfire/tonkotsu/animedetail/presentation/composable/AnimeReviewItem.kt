package com.nightfire.tonkotsu.ui.composables

// ... (existing imports, including Chip if you use it, or Badge)
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
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
import kotlin.collections.forEachIndexed
import androidx.compose.ui.graphics.vector.ImageVector
data class Reaction(val count: Int, val icon: ImageVector, val description: String)

fun getTopReactions(review: AnimeReview, limit: Int = 3): List<Reaction> {
    val reactions = mutableListOf<Reaction>()

    review.reactions.nice.takeIf { it > 0 }?.let {
        reactions.add(Reaction(it, Icons.Filled.ThumbUp, "Nice"))
    }
    review.reactions.loveIt.takeIf { it > 0 }?.let {
        reactions.add(Reaction(it, Icons.Filled.Favorite, "Love it"))
    }
    review.reactions.funny.takeIf { it > 0 }?.let {
        reactions.add(Reaction(it, Icons.Filled.SentimentSatisfiedAlt, "Funny"))
    }
    review.reactions.confusing.takeIf { it > 0 }?.let {
        reactions.add(Reaction(it, Icons.Filled.QuestionMark, "Confusing"))
    }
    review.reactions.informative.takeIf { it > 0 }?.let {
        reactions.add(Reaction(it, Icons.Filled.Lightbulb, "Informative"))
    }

    return reactions.sortedByDescending { it.count }.take(limit)
}

@Composable
fun AnimeReviewItem(
    review: AnimeReview, // Changed from AnimeReview to Review
    modifier: Modifier = Modifier,
    itemWidth: Dp = 300.dp // Define a default width for items in a row
) {
    val context = LocalContext.current
    val maxLines = 4 // Max lines for review content when collapsed
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }
    val topReactions = remember(review) { getTopReactions(review) }

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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Combine date and episodes watched if available
                    val reviewMeta = buildAnnotatedString {
                        review.date?.let { append(it.format(dateFormatter)) }
                        review.episodesWatched?.let { // Use episodesWatched from review.user
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

            Spacer(Modifier.height(10.dp))

            // Review Content
            review.reviewContent?.let { content -> // Use review.content from your Review model
                val reviewText = if (review.isSpoiler) {
                    "This review contains spoilers. Tap to read." // Simplified message
                } else {
                    content
                }
                Text(
                    text = reviewText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = maxLines, // Limit max lines when collapsed
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp), // Add some padding from the content above
                horizontalArrangement = Arrangement.SpaceBetween, // Distribute space
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (topReactions.isNotEmpty() && review.reactions.overall > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        topReactions.forEachIndexed { index, reaction ->
                            Icon(
                                imageVector = reaction.icon,
                                contentDescription = reaction.description,
                                tint = MaterialTheme.colorScheme.primary, // Or a specific reaction color
                                modifier = Modifier
                                    .size(20.dp) // Small icon size
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape) // Background to make overlap clear
                                    .clip(CircleShape)
                                    .padding(2.dp) // Padding inside the circle background
                            )
                        }

                        Text(
                            text = "${review.reactions.overall}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End, // Justify to the end (right)
                    verticalAlignment = Alignment.CenterVertically
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
                        Spacer(Modifier.width(4.dp)) // Spacer between chips if both exist
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
        }
    }
}