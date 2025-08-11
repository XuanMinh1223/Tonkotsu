package com.nightfire.tonkotsu.ui.fullscreenoverlay

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.nightfire.tonkotsu.core.domain.model.ReviewReactions

@Composable
fun ReviewItemFullScreen(
    content: OverlayContent.ReviewFullScreen,
    modifier: Modifier = Modifier,
) {
    val review = content.review
    val fadeHeight = 48.dp
    with(LocalDensity.current) { fadeHeight.toPx() }
    val fadeColor = MaterialTheme.colorScheme.background

    Box(modifier = modifier.fillMaxSize()) {
        // Scrollable text content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (review.isSpoiler || review.isPreliminary) {
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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

            review.reviewContent?.let { content ->
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            review.reviewUrl?.let { url ->
                ReadMoreButton(
                    modifier = Modifier.padding(top = 16.dp),
                    url = url,
                    )
            }

            ReviewReactionsDirect(
                reactions = review.reactions,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Fixed fade overlays on top and bottom edges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fadeHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            fadeColor,
                            fadeColor.copy(alpha = 0f)
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fadeHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            fadeColor.copy(alpha = 0f),
                            fadeColor
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun ReviewReactionsDirect(
    reactions: ReviewReactions,
    modifier: Modifier = Modifier,
) {
    val reactionItems = listOf(
        Triple(reactions.nice, Icons.Filled.ThumbUp, "Nice"),
        Triple(reactions.loveIt, Icons.Filled.Favorite, "Love it"),
        Triple(reactions.funny, Icons.Filled.SentimentSatisfiedAlt, "Funny"),
        Triple(reactions.confusing, Icons.Filled.QuestionMark, "Confusing"),
        Triple(reactions.informative, Icons.Filled.Lightbulb, "Informative"),
        Triple(reactions.wellWritten, Icons.Filled.Edit, "Well Written"),
        Triple(reactions.creative, Icons.Filled.AutoAwesome, "Creative"),
    ).filter { it.first > 0 }.sortedByDescending { it.first }

    if (reactionItems.isEmpty()) return

    FlowRow(
        modifier = modifier,
    ) {
        reactionItems.forEach { (count, icon, label) ->
            AssistChip(
                modifier = Modifier.padding(end = 4.dp),
                onClick = { },
                label = { Text("$count $label") },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                colors = AssistChipDefaults.assistChipColors()
            )
        }
    }
}

@Composable
fun ReadMoreButton(
    url: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {
        Text("Read on MyAnimeList")
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Open link"
        )
    }
}

