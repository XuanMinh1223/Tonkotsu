package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import java.time.LocalDate

@Composable
fun AnimeEpisodesList(
    uiState: UiState<List<AnimeEpisode>>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Episodes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Give it a fixed height for loading indicator
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "Failed to load episodes.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        } else {
            uiState.data?.let { episodes ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .shadow(2.dp, MaterialTheme.shapes.medium)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(episodes.size) { index ->
                        EpisodeListItem(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            episode = episodes[index],
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun EpisodeListSectionSuccessPreview() {
    val mockEpisodes = listOf(
        AnimeEpisode(
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
        AnimeEpisode(
            malId = 2,
            title = "Forest of Whispers",
            titleJapanese = "ささやきの森",
            titleRomanji = "Sasayaki no Mori",
            airedDate = LocalDate.of(2023, 10, 13),
            score = 8.2,
            isFiller = true, // Example filler episode
            isRecap = false,
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode(
            malId = 3,
            title = "Memory of a Hero",
            titleJapanese = "英雄の記憶",
            titleRomanji = "Eiyuu no Kioku",
            airedDate = LocalDate.of(2023, 10, 20),
            score = 8.7,
            isFiller = false,
            isRecap = true, // Example recap episode
            episodeUrl = null,
            forumUrl = null
        ),
        AnimeEpisode(
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
        AnimeEpisode(
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
                uiState = UiState.success(mockEpisodes)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun EpisodeListSectionLoadingPreview() {
    MaterialTheme {
        Surface {
            AnimeEpisodesList(uiState = UiState.loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun EpisodeListSectionErrorPreview() {
    MaterialTheme {
        Surface {
            AnimeEpisodesList(uiState = UiState.error("Failed to fetch episodes. Please try again."))
        }
    }
}