package com.nightfire.tonkotsu.ui // Adjust your package as needed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Use items directly if you don't need index
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState // Import the sealed UiState
import com.nightfire.tonkotsu.core.domain.model.Video // Import your Video domain model
import androidx.compose.material3.Surface // For previews

@Composable
fun VideoList(
    uiState: UiState<List<Video>>,
    modifier: Modifier = Modifier,
    onVideoClick: (Video) -> Unit = {} // Callback now takes a Video object
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
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
                        items(videos) { video -> // Iterate directly over video objects
                            YouTubeThumbnail(
                                video = video, // Pass the full Video object
                                onVideoClick = onVideoClick // Pass the callback down
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    if (uiState.isRetrying) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        }
                    } else {
                        Button(onClick = { /* ViewModel.retryFetchVideos() */ }) { // You'd need a retry function in ViewModel
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}

// --- Previews (You'd add these to test different states) ---
@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun VideoListSuccessPreview() {
    MaterialTheme {
        Surface {
            // Assuming YouTubeThumbnail is accessible or mocked for preview
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
            VideoList(uiState = UiState.Error("Failed to load videos.", isRetrying = false))
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