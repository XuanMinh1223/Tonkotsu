package com.nightfire.tonkotsu.animedetail.presentation.composable

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.animedetail.presentation.AnimeDetailViewModel
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.core.domain.model.StreamingService
import com.nightfire.tonkotsu.ui.ExpandableText
import com.nightfire.tonkotsu.ui.InfoRow
import com.nightfire.tonkotsu.ui.ScoreDisplayCard
import com.nightfire.tonkotsu.ui.TagSection
import java.util.Locale

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

@OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class) // For FlowRow, which TagSection uses
@Composable
fun AnimeDetailScreenContent(
    state: UiState<AnimeDetail>,
    modifier: Modifier = Modifier,
    onGenreClick: (String) -> Unit = {} // For clickable genres
) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.errorMessage != null) {
            Text(
                text = state.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            state.data?.let { anime ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top // Align items to the top of the row
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f) // This column will take up all available space after the image
                                    .padding(end = 8.dp) // Add some spacing between the titles and the image
                            ) {
                                Text(
                                    text = anime.title,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                anime.alternativeTitle?.let { altTitle -> // <--- Using alternativeTitle
                                    Text(
                                        text = altTitle,
                                        modifier = Modifier.padding(top = 8.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                anime.japaneseTitle?.let { jpTitle -> // <--- Using japaneseTitle
                                    Text(
                                        text = jpTitle,
                                        modifier = Modifier.padding(top = 8.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            AsyncImage(
                                model = anime.imageUrl,
                                contentDescription = "${anime.title} Poster",
                                modifier = Modifier
                                    .width(150.dp),
                                contentScale = ContentScale.Fit, // As requested, will fit entire image (may pillarbox)
                                alignment = Alignment.TopCenter // Ensures the top of the image is visible if it's tall
                            )
                        }

                        // --- 2. Core Stats ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ScoreDisplayCard(
                                score = anime.score,
                                scoredBy = anime.scoredBy
                            )
                            Column(
                                horizontalAlignment = Alignment.End, // Right-justify text within this column
                                verticalArrangement = Arrangement.spacedBy(4.dp) // Space between these stats
                            ) {
                                anime.rank?.let {
                                    Text(
                                        text = "Rank: #$it",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                anime.popularity?.let {
                                    Text(
                                        text = "Popularity: #$it",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                anime.favorites?.let {
                                    Text(
                                        text = "Favorites: ${String.format(Locale.US, "%,d", it)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(16.dp))

                        // --- 3. Key Info ---
                        InfoRow(label = "Type:", value = anime.type)
                        InfoRow(label = "Episodes:", value = anime.episodes?.toString())
                        InfoRow(label = "Status:", value = anime.status)
                        // Airing status was a boolean in old model, now just check if status is "Airing"
                        InfoRow(label = "Duration:", value = anime.duration)
                        InfoRow(label = "Rating:", value = anime.rating)
                        InfoRow(label = "Source:", value = anime.source)
                        InfoRow(label = "Season:", value = "${anime.season ?: ""} ${anime.year ?: ""}".trim())
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(16.dp))
                        TagSection(title = "Genres:", tags = anime.genres, onTagClick = onGenreClick)
                        HorizontalDivider()
                        Spacer(Modifier.height(16.dp))

                        // --- 4. Synopsis & Background (with "Read More") ---
                        ExpandableText(
                            title = "Synopsis",
                            text = anime.synopsis
                        )

                        ExpandableText(
                            title = "Background",
                            text = anime.background,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(16.dp))

                        // --- 6. Production Details (FlowRows using TagSection) ---
                        TagSection(title = "Studios:", tags = anime.studios)
                        TagSection(title = "Producers:", tags = anime.producers)
                        TagSection(title = "Licensors:", tags = anime.licensors)
                        Spacer(Modifier.height(16.dp))

                        // --- 7. External Media & Streaming Services ---
                        anime.trailerYoutubeId?.let { youtubeId ->
                            HorizontalDivider()
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW,
                                        "https://www.youtube.com/watch?v=$youtubeId".toUri())
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Watch Trailer")
                                Spacer(Modifier.width(8.dp))
                                Text("Watch Trailer")
                            }
                            Spacer(Modifier.height(16.dp))
                        }

                        anime.streamingServices.takeIf { it.isNotEmpty() }?.let { services ->
                            Text(
                                text = "Streaming On:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                services.forEach { service ->
                                    // Assuming StreamingService has a 'name' property
                                    Text(
                                        text = service.name, // Adjust if StreamingService has a different display field
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.tertiaryContainer,
                                                MaterialTheme.shapes.small
                                            )
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }


                        // --- 8. Themes (Opening/Ending) ---
                        anime.openingThemes.takeIf { it.isNotEmpty() }?.let { themes ->
                            Divider()
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Opening Themes:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                themes.forEach { theme ->
                                    Text(text = "• $theme", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }

                        anime.endingThemes.takeIf { it.isNotEmpty() }?.let { themes ->
                            Text(
                                text = "Ending Themes:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                themes.forEach { theme ->
                                    Text(text = "• $theme", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }

                        // --- 9. Relations (Basic Display) ---
                        anime.relations.takeIf { it.isNotEmpty() }?.let { relationsMap ->
                            HorizontalDivider()
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Relations:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(4.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                relationsMap.forEach { (type, entries) ->
                                    Text(
                                        text = "$type:",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                    entries.forEach { entry ->
                                        Text(
                                            text = "• ${entry.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 1000) // Set a reasonable height for scrolling
@Composable
fun AnimeDetailScreenContentSuccessPreview() {
    // Define a mock AnimeDetail object
    val mockAnimeDetail = AnimeDetail(
        id = 1,
        title = "Frieren: Beyond Journey's End",
        imageUrl = "https://cdn.myanimelist.net/images/anime/10/89392l.jpg", // Use a real image URL for preview
        score = 9.38,
        scoredBy = 1234567,
        rank = 1,
        popularity = 5,
        favorites = 87654,
        type = "TV",
        episodes = 28,
        status = "Finished Airing",
        duration = "24 min per ep",
        rating = "PG-13",
        source = "Manga",
        season = "Fall",
        year = 2023,
        synopsis = "Frieren, a seemingly immortal elf, sets off on a journey to understand humanity after her former party members pass away. She reflects on her past adventures and learns about the fleeting nature of life through new companions. This synopsis is long enough to test the expandable text feature and ensure it handles multiple lines gracefully. It provides a good overview of the series' themes and premise, hinting at the emotional depth and character development that unfolds throughout her journey.",
        background = "Based on the manga written by Kanehito Yamada and illustrated by Tsukasa Abe. It won the Grand Prize for manga at the 2021 Tezuka Osamu Cultural Prize and the 2021 Manga Taisho award. The anime adaptation was highly anticipated and received critical acclaim for its stunning animation, faithful adaptation of the source material, and heartfelt storytelling.",
        genres = listOf("Adventure", "Fantasy", "Slice of Life", "Drama", "Magic"),
        studios = listOf("Madhouse"),
        producers = listOf("Aniplex", "Shogakukan-Shueisha Productions", "Dentsu"),
        licensors = listOf("Crunchyroll", "Funimation"),
        trailerYoutubeId = "v6QkYchc710", // Example YouTube ID
        streamingServices = listOf(
            StreamingService(
                "Crunchyroll",
                "https://www.crunchyroll.com/series/G4VNY2882/frieren-beyond-journeys-end"
            ),
            StreamingService("Netflix", "netflixurl"),
            StreamingService("Hulu", "https://www.hulu.com/series/frieren")
        ),
        openingThemes = listOf(
            "\"Yuusha\" by YOASOBI",
            "\"Frieren's Journey\" by Aimer"
        ),
        endingThemes = listOf(
            "\"Anytime Anywhere\" by milet",
            "\"Blossom\" by Eve"
        ),
        relations = mapOf(
            "Prequel" to listOf(RelationEntry(234, "Frieren: Journey's Beginning", "anime","")),
            "Adaptation" to listOf(RelationEntry(567, "Sousou no Frieren", "manga","")),
            "Side Story" to listOf(RelationEntry(789, "Frieren: Daily Life", "manga",""))
        ),
        members = null,
        alternativeTitle = "alternative title",
        japaneseTitle = "japanese title",
    )

    MaterialTheme { // Wrap with your app's MaterialTheme for correct colors and typography
        Surface { // Provide a Surface to ensure the background matches your theme
            AnimeDetailScreenContent(
                state = UiState.success(mockAnimeDetail),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun AnimeDetailScreenContentLoadingPreview() {
    MaterialTheme {
        Surface {
            AnimeDetailScreenContent(state = UiState.loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun AnimeDetailScreenContentErrorPreview() {
    MaterialTheme {
        Surface {
            AnimeDetailScreenContent(state = UiState.error("Failed to load anime details. Check your internet connection."))
        }
    }
}

