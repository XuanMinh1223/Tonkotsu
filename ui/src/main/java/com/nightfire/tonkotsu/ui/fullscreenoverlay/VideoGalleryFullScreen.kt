package com.nightfire.tonkotsu.ui.fullscreenoverlay

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VideoGalleryFullScreen(
    content: OverlayContent.VideoFullScreen,
    modifier: Modifier = Modifier,
    pagerHeightFraction: Float = 0.5f
) {
    if (content.videos.isEmpty()) {
        Text(
            text = "No videos to display.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )
        return
    }

    val pagerState = rememberPagerState(
        initialPage = content.initialIndex.coerceIn(0, content.videos.size - 1),
        pageCount = { content.videos.size }
    )

    var controlsVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val resetControlsTimer: () -> Unit = {
        coroutineScope.launch {
            controlsVisible = true
            delay(3000L)
            controlsVisible = false
        }
    }

    LaunchedEffect(Unit) { resetControlsTimer() }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collect { isScrolling ->
            controlsVisible = isScrolling || controlsVisible
            if (!isScrolling) resetControlsTimer()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(pagerHeightFraction)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        controlsVisible = !controlsVisible
                        if (controlsVisible) resetControlsTimer()
                    }
                )
            }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
        ) { page ->
            val video = content.videos[page]
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        loadUrl(video.videoUrl)
                    }
                },
                update = { /* update if needed */ }
            )
        }

        // Left Arrow
        AnimatedVisibility(
            visible = controlsVisible && pagerState.currentPage > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        resetControlsTimer()
                    }
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous video",
                    tint = Color.White
                )
            }
        }

        // Right Arrow
        AnimatedVisibility(
            visible = controlsVisible && pagerState.currentPage < content.videos.size - 1,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        resetControlsTimer()
                    }
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next video",
                    tint = Color.White
                )
            }
        }

        // Page indicator only if more than one video
        if (content.videos.size > 1) {
            AnimatedVisibility(
                visible = controlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${content.videos.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

