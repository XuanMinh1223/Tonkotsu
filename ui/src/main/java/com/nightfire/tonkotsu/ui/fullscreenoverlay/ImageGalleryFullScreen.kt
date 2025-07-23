package com.nightfire.tonkotsu.ui.fullscreenoverlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ImageGalleryFullScreen(
    content: OverlayContent.ImageGalleryFullScreen
) {
    val pagerState = rememberPagerState(
        initialPage = content.initialIndex,
        pageCount = { content.images.size }
    )

    var controlsVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Function to reset controls visibility timer
    val resetControlsTimer: () -> Unit = {
        coroutineScope.launch {
            controlsVisible = true
            delay(3000L)
            controlsVisible = false
        }
    }

    // Initially hide controls after a delay
    LaunchedEffect(Unit) {
        resetControlsTimer()
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling) {
                    controlsVisible = true
                } else {
                    // Restart the hide timer when scrolling ends
                    resetControlsTimer()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        controlsVisible = !controlsVisible
                        if (controlsVisible) resetControlsTimer()
                    }
                )
            }
    ) {
        // The horizontal pager that allows swipe gestures and shows images
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = content.images[page].url,
                contentDescription = "Gallery image ${page + 1} of ${content.images.size}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Left Arrow (Animated Visibility)
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
                    contentDescription = "Previous image",
                    tint = Color.White
                )
            }
        }

        // Right Arrow (Animated Visibility)
        AnimatedVisibility(
            visible = controlsVisible && pagerState.currentPage < content.images.size - 1,
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
                    contentDescription = "Next image",
                    tint = Color.White
                )
            }
        }

        // Image Index Indicator (Animated Visibility)
        AnimatedVisibility(
            visible = controlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "${pagerState.currentPage + 1} / ${content.images.size}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
