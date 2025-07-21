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
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.Video // Import your Video domain model

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

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        } else if (uiState.errorMessage != null) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.errorMessage!!, // Consider safer non-null assertion
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (uiState.isRetrying) { // Assuming isRetrying is part of UiState.Error
                    Text(
                        text = "Retrying shortly...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Button(onClick = { /* ViewModel.retryFetchVideos() */ }) { // You'd need a retry function in ViewModel
                        Text("Try Again")
                    }
                }
            }
        } else {
            val videos = uiState.data
            if (videos.isNullOrEmpty()) {
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
    }
}