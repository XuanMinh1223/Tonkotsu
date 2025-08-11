package com.nightfire.tonkotsu.ui.fullscreenoverlay

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Video
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@SuppressLint("SetJavaScriptEnabled") // WebView requires JavaScript to be enabled for YouTube embeds
@Composable
fun FullScreenOverlay(
    content: OverlayContent,
    onDismiss: () -> Unit
) {
    // Controls animation for the overlay
    var isVisible by remember { mutableStateOf(false) } // State for animation

    BackHandler(enabled = isVisible) {
        isVisible = false
    }

    LaunchedEffect(Unit) {
        isVisible = true // Trigger animation when composable enters composition
    }

    LaunchedEffect(isVisible) {
        if (!isVisible) {
            delay(300) // duration of exit animation
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter =  fadeIn(),
        exit =  fadeOut()
    ) {
        Surface( // Use Surface for a consistent background and elevation
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background), // Use background color from theme
            color = MaterialTheme.colorScheme.background // Set surface color too
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar with Close button

                if (content is OverlayContent.ReviewFullScreen) {
                    ReviewTopBar(
                        onCloseClick = { isVisible = false },
                        review = content.review
                    )
                } else {
                    DefaultTopBar(
                        onCloseClick = { isVisible = false }
                    )
                }


                // Content Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (content) {
                        is OverlayContent.ImageGalleryFullScreen -> {
                            ImageGalleryFullScreen(content)
                        }
                        is OverlayContent.VideoFullScreen -> {
                            VideoGalleryFullScreen(
                                content = content
                            )
                        }
                        is OverlayContent.ReviewFullScreen -> {
                            ReviewItemFullScreen(
                                content = content
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close overlay",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ReviewTopBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    review: AnimeReview,
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
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
                ElevatedCard(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .wrapContentWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Score",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp) // Larger star icon
                        )
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.headlineSmall, // Larger score text
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close overlay",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
// --- Previews ---

@Preview(showBackground = true)
@Composable
fun FullScreenVideoOverlayPreview() {
    MaterialTheme {
        Surface {
            FullScreenOverlay(
                content = OverlayContent.VideoFullScreen(
                    listOf(Video(
                        videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                        thumbnailUrl = null // Not used for playback, but part of the model
                    )),
                    title = "Example Anime Trailer"
                ),
                onDismiss = {}
            )
        }
    }
}