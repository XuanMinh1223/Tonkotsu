package com.nightfire.tonkotsu.ui.composables // Adjust your package as needed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.YouTubeThumbnail
import com.nightfire.tonkotsu.ui.shimmerEffect

@Composable
fun VideoList(
    uiState: UiState<List<Video>>,
    modifier: Modifier = Modifier,
    onVideoClick: (Video, Int) -> Unit = { _, _ -> } // Callback now takes Video and Int
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Videos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (uiState) { // Use 'when' with the sealed UiState
            is UiState.Loading -> {
                VideoListSkeleton()
            }
            is UiState.Success -> {
                val videos = uiState.data // 'data' is directly accessible and non-null here
                if (videos.isEmpty()) { // Check if the list itself is empty
                    Text(
                        text = "No videos available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(videos) { index, video -> // Use itemsIndexed to get the index
                            YouTubeThumbnail(
                                video = video,
                                index = index, // Pass the index to YouTubeThumbnail
                                onVideoClick = onVideoClick // Pass the callback down
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                ErrorCard(
                    message = uiState.message, // 'message' is directly accessible
                    modifier = Modifier.padding(16.dp),
                    actionButtonText = "Retry",
                )
            }
        }
    }
}

@Composable
fun VideoListSkeleton() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(200.dp) // Match YouTubeThumbnail width
                    .height(112.dp) // Match YouTubeThumbnail 16:9 aspect ratio (200 * 9 / 16)
                    .clip(RoundedCornerShape(4.dp)) // Match YouTubeThumbnail corner radius
                    .shimmerEffect() // Apply the shimmer effect
            )
        }
    }
}

// --- Previews (You'd add these to test different states) ---
@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun VideoListSuccessPreview() {
    MaterialTheme {
        Surface {
            val mockVideos = listOf(
                Video(videoUrl = "https://www.youtube.com/watch?v=video1", thumbnailUrl = "https://img.youtube.com/vi/video1/hqdefault.jpg"),
                Video(videoUrl = "https://www.youtube.com/watch?v=video2", thumbnailUrl = "https://img.youtube.com/vi/video2/hqdefault.jpg"),
                Video(videoUrl = "https://www.youtube.com/watch?v=video3", thumbnailUrl = "https://img.youtube.com/vi/video3/hqdefault.jpg"),
            )
            VideoList(uiState = UiState.Success(mockVideos))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun VideoListLoadingPreview() {
    MaterialTheme {
        Surface {
            VideoList(uiState = UiState.Loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun VideoListErrorPreview() {
    MaterialTheme {
        Surface {
            VideoList(uiState = UiState.Error("Failed to load videos.", ))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun VideoListEmptyPreview() {
    MaterialTheme {
        Surface {
            VideoList(uiState = UiState.Success(emptyList()))
        }
    }
}