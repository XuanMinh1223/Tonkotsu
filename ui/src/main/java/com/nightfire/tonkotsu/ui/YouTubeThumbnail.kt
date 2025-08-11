package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.core.domain.model.Video // Import your Video domain model

@Composable
fun YouTubeThumbnail(
    video: Video, // Pass the full Video object
    index: Int, // Pass the index of this video in its list
    onVideoClick: (Video, Int) -> Unit = { _, _ -> } // Callback now takes Video and Int
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onVideoClick(video, index) } // Pass the full Video object and index on click
    ) {
        video.thumbnailUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = "YouTube Thumbnail for ${video.videoUrl}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } ?: run {
            Image(
                painter = painterResource(R.drawable.default_thumbnail),
                contentDescription = "Thumbnail Unavailable",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play ${video.videoUrl}",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                .padding(8.dp)
        )
    }
}
