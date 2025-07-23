package com.nightfire.tonkotsu.ui.composables // Adjusted package for common composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.animedetail.presentation.composable.EpisodeListItem
import com.nightfire.tonkotsu.core.common.UiState // Import the sealed UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import java.time.LocalDate

@Composable
fun AnimeEpisodesList(
    uiState: UiState<List<AnimeEpisode>>, // Changed from AnimeEpisode to Episode
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Episodes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (uiState) { // Use 'when' with the sealed UiState
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Give it a fixed height for loading indicator
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }
            is UiState.Success -> {
                val episodes = uiState.data // 'data' is directly accessible and non-null here
                if (episodes.isNullOrEmpty()) { // Check if the list itself is empty
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp) // Consistent height for empty state
                            .padding(vertical = 16.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "No episodes icon",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "No episodes found for this anime.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp) // Fixed height for the scrollable list
                            .shadow(2.dp, MaterialTheme.shapes.medium)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(episodes) { episode -> // Iterate directly over episode objects
                            EpisodeListItem(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                episode = episode,
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline
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
                        text = uiState.message, // 'message' is directly accessible
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    if (uiState.isRetrying) { // 'isRetrying' is directly accessible
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        }
                    } else {
                        // This is a final error, perhaps show a "Retry" button
                        Button(onClick = { /* ViewModel.retryFetchEpisodes() */ }) { // You'd need a retry function in ViewModel
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}

// --- Previews ---

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun EpisodeListSectionSuccessPreview() {
    val mockEpisodes = listOf(
        AnimeEpisode( // Changed from AnimeEpisode to Episode
            malId = 1,
            title = "The Journey Begins",
            titleJapanese = "旅の始まり",
            titleRomanji = "Tabi no Hajimari",
            airedDate = LocalDate.of(2023, 10, 6),
            score = 8.5,
            isFiller = false,
            isRecap = false,
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode( // Changed from AnimeEpisode to Episode
            malId = 2,
            title = "Forest of Whispers",
            titleJapanese = "ささやきの森",
            titleRomanji = "Sasayaki no Mori",
            airedDate = LocalDate.of(2023, 10, 13),
            score = 8.2,
            isFiller = true,
            isRecap = false,
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode( // Changed from AnimeEpisode to Episode
            malId = 3,
            title = "Memory of a Hero",
            titleJapanese = "英雄の記憶",
            titleRomanji = "Eiyuu no Kioku",
            airedDate = LocalDate.of(2023, 10, 20),
            score = 8.7,
            isFiller = false,
            isRecap = true,
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode( // Changed from AnimeEpisode to Episode
            malId = 4,
            title = "New Companions",
            titleJapanese = "新たな仲間",
            titleRomanji = "Arata na Nakama",
            airedDate = LocalDate.of(2023, 10, 27),
            score = 8.9,
            isFiller = false,
            isRecap = false,
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode( // Changed from AnimeEpisode to Episode
            malId = 5,
            title = "Clash of Fate",
            titleJapanese = "運命の衝突",
            titleRomanji = "Unmei no Shoutotsu",
            airedDate = LocalDate.of(2023, 11, 3),
            score = 9.0,
            isFiller = false,
            isRecap = false,
            episodeUrl = null,
            forumUrl = null
        )
    )

    MaterialTheme {
        Surface {
            AnimeEpisodesList(
                uiState = UiState.Success(mockEpisodes) // Updated constructor
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun EpisodeListSectionLoadingPreview() {
    MaterialTheme {
        Surface {
            AnimeEpisodesList(uiState = UiState.Loading()) // Updated constructor
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun EpisodeListSectionErrorPreview() {
    MaterialTheme {
        Surface {
            // Updated to reflect the new UiState.Error constructor
            AnimeEpisodesList(uiState = UiState.Error("Failed to fetch episodes. Please try again.", isRetrying = false))
        }
    }
}
