package com.nightfire.tonkotsu.ui.fullscreenoverlay

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.ui.fullscreenoverlay.OverlayContent.ReviewFullScreen
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled") // WebView requires JavaScript to be enabled for YouTube embeds
@Composable
fun FullScreenOverlay(
    content: OverlayContent,
    onDismiss: () -> Unit
) {
    // Controls animation for the overlay
    val density = LocalDensity.current
    var isVisible by remember { mutableStateOf(false) } // State for animation

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { isVisible = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close overlay",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Content Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Padding for the content itself
                    contentAlignment = Alignment.Center // Center content by default
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
                            ReviewGalleryFullScreen(
                                content = content
                            )
                        }
                    }
                }
            }
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

@Preview(showBackground = true)
@Composable
fun FullScreenReviewOverlayPreview() {
    MaterialTheme {
        Surface {

        }
    }
}