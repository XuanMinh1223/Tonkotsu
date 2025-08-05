package com.nightfire.tonkotsu.ui.fullscreenoverlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReviewGalleryFullScreen(
    content: OverlayContent.ReviewFullScreen, // Changed parameter type
    modifier: Modifier = Modifier,
) {
    if (content.reviews.isEmpty()) {
        Text(
            text = "No reviews to display.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )
        return
    }

    val pagerState = rememberPagerState(
        initialPage = content.initialIndex.coerceIn(0, content.reviews.size - 1),
        pageCount = { content.reviews.size }
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

    // Initially hide controls after a delay
    LaunchedEffect(Unit) { resetControlsTimer() }

    // Observe pager scroll state to manage controls visibility
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collect { isScrolling ->
            if (isScrolling) {
                controlsVisible = true
            } else {
                resetControlsTimer() // Restart the hide timer when scrolling ends
            }
        }
        // Also reset timer when page changes (e.g., via button click)
        snapshotFlow { pagerState.currentPage }.collect {
            resetControlsTimer()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight() // Allocate height for the pager
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
            ReviewItemFullScreen(content = content, currentPage = page)
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
                    contentDescription = "Previous review",
                    tint = Color.White
                )
            }
        }

        // Right Arrow
        AnimatedVisibility(
            visible = controlsVisible && pagerState.currentPage < content.reviews.size - 1,
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
                    contentDescription = "Next review",
                    tint = Color.White
                )
            }
        }
    }
}