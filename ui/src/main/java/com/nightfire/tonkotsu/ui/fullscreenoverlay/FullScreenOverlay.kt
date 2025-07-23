package com.nightfire.tonkotsu.ui.fullscreenoverlay

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video

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

    // Capture lifecycle to manage WebView state (pause/resume)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // Pause WebView when app goes to background
                    // This logic assumes the WebView itself will be paused through AndroidView's update
                    // For more direct control, you might need to manage WebView state via a reference.
                }

                Lifecycle.Event.ON_RESUME -> {
                    // Resume WebView when app comes to foreground
                    // This logic assumes the WebView itself will be resumed through AndroidView's update
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                    IconButton(onClick = onDismiss) {
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
                        is OverlayContent.ImageFullScreen -> {
                            AsyncImage(
                                model = content.image.url,
                                contentDescription = "Full screen image", // More descriptive content description
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit // Fit the entire image
                            )
                        }
                        is OverlayContent.ImageGalleryFullScreen -> {
                            ImageGalleryFullScreen(content)
                        }
                        is OverlayContent.VideoFullScreen -> {
                            // Use AndroidView to embed a WebView for video playback
                            // Prioritize embedUrl for YouTube if available
                            val videoToLoad = content.video.videoUrl

                            if (videoToLoad.isNotBlank()) {
                                AndroidView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f) // Take half of the available height for video
                                        .clip(MaterialTheme.shapes.medium),
                                    factory = { context ->
                                        WebView(context).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            webViewClient = WebViewClient()
                                            webChromeClient =
                                                WebChromeClient() // Needed for full-screen video
                                            settings.javaScriptEnabled = true // Enable JavaScript for YouTube embeds
                                            settings.loadWithOverviewMode = true
                                            settings.useWideViewPort = true
                                            loadUrl(videoToLoad)
                                        }
                                    },
                                    update = { webView ->
                                        // Update logic here if URL changes (though for this overlay, it won't)
                                        // You could also use webView.loadUrl(videoToLoad) here if the URL might change.
                                    }
                                )
                                content.title?.let { title ->
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.BottomCenter) // Position title below video
                                    )
                                }
                            } else {
                                Text(
                                    text = "Video URL not available.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        is OverlayContent.ReviewFullScreen -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = content.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "by ${content.author}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = content.content,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
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
fun FullScreenImageOverlayPreview() {
    MaterialTheme {
        Surface {
            FullScreenOverlay(
                content = OverlayContent.ImageFullScreen(
                    Image(url = "https://cdn.myanimelist.net/images/anime/10/78745.jpg")
                ),
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreenVideoOverlayPreview() {
    MaterialTheme {
        Surface {
            FullScreenOverlay(
                content = OverlayContent.VideoFullScreen(
                    Video(
                        videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                        thumbnailUrl = null // Not used for playback, but part of the model
                    ),
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
            FullScreenOverlay(
                content = OverlayContent.ReviewFullScreen(
                    title = "An Epic Journey",
                    content = "This anime is a masterpiece! The story depth, character development, and animation quality are top-notch. I particularly enjoyed the philosophical themes explored throughout the series. Every episode left me wanting more. Highly recommended for fans of fantasy and adventure genres. The ending was truly satisfying and brought a great conclusion to all the character arcs.",
                    author = "AnimeFanatic99"
                ),
                onDismiss = {}
            )
        }
    }
}