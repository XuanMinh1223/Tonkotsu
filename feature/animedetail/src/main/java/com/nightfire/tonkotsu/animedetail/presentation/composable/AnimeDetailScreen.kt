package com.nightfire.tonkotsu.animedetail.presentation.composable

import android.widget.ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.animedetail.presentation.AnimeDetailViewModel
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail

@Composable
fun AnimeDetailScreen(
    malId: Int,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {

    val animeDetailState by viewModel.animeDetailState.collectAsState()

    LaunchedEffect(key1 = malId) {
        viewModel.getAnimeDetail(malId)
    }
    Scaffold(modifier = Modifier.systemBarsPadding()) { innerPadding ->
        AnimeDetailScreenContent(
            state = animeDetailState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AnimeDetailScreenContent(
    state: UiState<AnimeDetail>, // Changed to UiState<AnimeDetail>
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.errorMessage != null) {
            // Display error message if available
            Text(
                text = state.errorMessage!!, // Directly access errorMessage
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Display data if not loading and no error
            state.data?.let { anime ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // 1. Anime Image
                    AsyncImage(
                        model = anime.imageUrl,
                        contentDescription = "${anime.title} Poster",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))

                    // 2. Titles
                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "English: ${anime.title}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))

                    // 3. Key Stats (Score, Rank, Popularity, Members)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        anime.score?.let {
                            Text(
                                text = "Score: ${it}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        anime.rank?.let {
                            Text(
                                text = "Rank: #$it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        anime.popularity?.let {
                            Text(
                                text = "Pop: #$it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        anime.members?.let {
                            Text(
                                text = "Members: ${String.format("%,d", it)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // 4. Basic Info (Type, Episodes, Status, Duration, Rating)
                    Text(text = "Episodes: ${anime.episodes ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Status: ${anime.status ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Duration: ${anime.duration ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Rating: ${anime.rating ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(16.dp))

                    // 5. Synopsis
                    Text(
                        text = "Synopsis:",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = anime.synopsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))

                    // 6. Background (if available)
                    anime.background?.let {
                        Text(
                            text = "Background:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    // 7. Genres (example)
                    anime.genres.takeIf { it.isNotEmpty() }?.let { genres ->
                        Text(
                            text = "Genres:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
