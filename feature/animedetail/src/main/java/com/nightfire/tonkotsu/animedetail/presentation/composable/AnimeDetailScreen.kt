package com.nightfire.tonkotsu.animedetail.presentation.composable // Adjust your package as needed

import CharacterListSection
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.animedetail.presentation.AnimeDetailViewModel
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.NavigableLink
import com.nightfire.tonkotsu.core.domain.model.Recommendation
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.ExpandableText
import com.nightfire.tonkotsu.ui.ImageList
import com.nightfire.tonkotsu.ui.R
import com.nightfire.tonkotsu.ui.ScoreDisplayCard
import com.nightfire.tonkotsu.ui.TagSection
import com.nightfire.tonkotsu.ui.composables.AnimeEpisodesList
import com.nightfire.tonkotsu.ui.composables.VideoList
import com.nightfire.tonkotsu.ui.fullscreenoverlay.FullScreenOverlay
import com.nightfire.tonkotsu.ui.fullscreenoverlay.OverlayContent
import java.util.Locale

@Composable
fun AnimeDetailScreen(
    malId: Int,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {

    val animeDetailState by viewModel.animeDetailState.collectAsState()
    val animeEpisodesState by viewModel.animeEpisodesState.collectAsState()
    val animeCharactersState by viewModel.animeCharactersState.collectAsState()
    val animeImagesState by viewModel.animeImagesState.collectAsState()
    val animeVideosState by viewModel.animeVideosState.collectAsState()
    val animeReviewsState by viewModel.animeReviewsState.collectAsState()
    val animeRecommendationState by viewModel.animeRecommendationsState.collectAsState()

    LaunchedEffect(key1 = malId) {
        viewModel.getAnimeDetail(malId)
    }
    Scaffold(modifier = Modifier.systemBarsPadding()) { innerPadding ->
        AnimeDetailScreenContent(
            animeDetailState = animeDetailState,
            animeEpisodesState = animeEpisodesState,
            animeCharactersState = animeCharactersState,
            animeImagesState = animeImagesState,
            animeVideosState = animeVideosState,
            animeReviewsState = animeReviewsState,
            animeRecommendationState = animeRecommendationState,
            onRecommendationClick = viewModel::onRecommendationClick,
            onRelationClick = viewModel::onRelationClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class) // For FlowRow, which TagSection uses
@Composable
fun AnimeDetailScreenContent(
    animeDetailState: UiState<AnimeDetail>,
    animeEpisodesState: UiState<List<AnimeEpisode>>,
    animeCharactersState: UiState<List<Character>>,
    animeImagesState: UiState<List<Image>>,
    animeVideosState: UiState<List<Video>>,
    animeReviewsState: UiState<List<AnimeReview>>,
    animeRecommendationState: UiState<List<Recommendation>>,
    modifier: Modifier = Modifier,
    onGenreClick: (String) -> Unit = {},
    onRecommendationClick: (Int) -> Unit = {},
    onRelationClick: (RelationEntry) -> Unit = {}
) {
    var overlayContent by rememberSaveable  { mutableStateOf<OverlayContent?>(null) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (animeDetailState) { // Use 'when' with the sealed UiState
            is UiState.Loading -> {
                AnimeDetailSkeletonScreen()
            }
            is UiState.Error -> {
                ErrorCard(
                    message = animeDetailState.message, // 'message' is directly accessible
                    modifier = Modifier.padding(16.dp),
                    actionButtonText = "Retry",
                )
            }
            is UiState.Success -> {
                val anime = animeDetailState.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AnimeTitleSection(
                            anime = anime,
                            onImageClick = {
                                overlayContent = OverlayContent.ImageGalleryFullScreen(
                                    images = listOf(Image(anime.imageUrl)),
                                    initialIndex = 0
                                )
                            }
                        )
                        // --- 2. Core Stats ---
                        AnimeCoreStats(anime = anime)
                        anime.trailerYoutubeId?.let { youtubeId ->
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    overlayContent = OverlayContent.VideoFullScreen(
                                        videos = listOf(Video(
                                            videoUrl = anime.trailerYoutubeUrl ?: "https://www.youtube.com/watch?v=$youtubeId",
                                            thumbnailUrl = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg" // Standard YouTube thumbnail
                                        )),
                                        title = "${anime.title} Trailer"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Watch Trailer")
                                Spacer(Modifier.width(8.dp))
                                Text("Watch Trailer")
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                        // --- 3. Key Info ---
                        AnimeKeyInfo(anime = anime)
                        Spacer(Modifier.height(16.dp)) // Add spacing after key info
                        AppHorizontalDivider()

                        TagSection(title = "Genres:", tags = anime.genres, onTagClick = onGenreClick)
                        TagSection(title = "Themes:", tags = anime.themes, onTagClick = onGenreClick)
                        TagSection(title = "Categories:", tags = anime.categories, onTagClick = onGenreClick)
                        Spacer(Modifier.height(16.dp)) // Add spacing after tags
                        AppHorizontalDivider()

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
                        Spacer(Modifier.height(16.dp)) // Add spacing after expandable texts
                        AppHorizontalDivider()
                        // --- 5. Production Details (FlowRows using TagSection) ---
                        TagSection(title = "Studios:", tags = anime.studios, isSecondary = true)
                        TagSection(title = "Producers:", tags = anime.producers, isSecondary = true)
                        TagSection(title = "Licensors:", tags = anime.licensors, isSecondary = true)
                        Spacer(Modifier.height(16.dp)) // Add spacing after production details

                        // --- 6. External Media & Streaming Services ---
                        // Removed the single "Watch Trailer" button, now handled by VideoList
                        AppHorizontalDivider()
                        VideoList(
                            uiState = animeVideosState,
                            onVideoClick = { clickedVideo, index -> // Now receives Video and Int
                                (animeVideosState as? UiState.Success)?.data?.let { videosList ->
                                    overlayContent = if (videosList.size == 1) {
                                        // If there's only one video in the list, open it directly
                                        OverlayContent.VideoFullScreen(
                                            videos = listOf(clickedVideo), // Still pass as a list of one
                                            initialIndex = 0,
                                            title = anime.title // Use anime title or clickedVideo.title if available
                                        )
                                    } else {
                                        // If multiple videos, open the gallery
                                        OverlayContent.VideoFullScreen(
                                            videos = videosList, // Pass the full list
                                            initialIndex = index, // Pass the clicked index
                                            title = anime.title // Use anime title or a gallery title
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp)) // Add spacing after video list
                        AppHorizontalDivider()
                        // Displaying Anime Images using the ImageList
                        ImageList(
                            uiState = animeImagesState,
                            onImageClick = { clickedImage, index ->
                                (animeImagesState as? UiState.Success)?.data?.let { imagesList ->
                                    overlayContent = OverlayContent.ImageGalleryFullScreen(
                                        images = imagesList,
                                        initialIndex = index
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp)) // Add spacing after image list
                        AppHorizontalDivider()

                        // Anime Episodes List
                        if (anime.type?.lowercase() != "movie") {
                            AnimeEpisodesList(animeEpisodesState)
                        }
                        // Character List Section
                        CharacterListSection(animeCharactersState)
                        Spacer(Modifier.height(16.dp)) // Add spacing after character list
                        AppHorizontalDivider()
                        AnimeReviewList(
                            uiState = animeReviewsState,
                            onReviewClick = { clickedReview, index ->
                                (animeReviewsState as? UiState.Success)?.data?.let { reviewsList ->
                                    overlayContent = OverlayContent.ReviewFullScreen(
                                        reviews = reviewsList,
                                        initialIndex = index
                                    )
                                }
                            }
                        )

                        RecommendationList(
                            uiState = animeRecommendationState,
                            onRecommendationClick = onRecommendationClick
                        )

                        anime.streamingLinks.takeIf { it.isNotEmpty() }?.let { links ->
                            AppHorizontalDivider()
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Streaming On:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            ExternalUrlSection(links, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                        }

                        // --- 7. Themes (Opening/Ending) ---
                        AnimeThemesSection(anime = anime)

                        // --- 8. Relations (Basic Display) ---
                        AnimeRelationsSection(
                            anime = anime,
                            onRelationClick = onRelationClick,
                        )
                    }
                    Spacer(Modifier.height(16.dp)) // Final bottom padding for the entire scrollable Column
                }
            }
        }
    }
    overlayContent?.let { content ->
        FullScreenOverlay(
            content = content,
            onDismiss = { overlayContent = null } // Dismisses the overlay by setting state to null
        )
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
        trailerYoutubeUrl = "v6QkYchc710", // Example YouTube ID
        trailerYoutubeId = "v6QkYchc710", // Example YouTube ID
        streamingLinks = listOf(
            NavigableLink(
                "Crunchyroll",
                "https://www.crunchyroll.com/series/G4VNY2882/frieren-beyond-journeys-end"
            ),
            NavigableLink("Netflix", "netflixurl"),
            NavigableLink("Hulu", "https://www.hulu.com/series/frieren")
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
        premiereDate = null,
        endDate = null,
        broadcast = "Monday, 12:00 JST",
        themes = listOf("theme1", "theme2"),
        categories = listOf("category1", "category2"),
        externalLinks = listOf(
            NavigableLink(
                "External Link",
                "https://www.crunchyroll.com/series/G4VNY2882/frieren-beyond-journeys-end"
            ),
            NavigableLink("Link 2", "netflixurl"),
            NavigableLink("Social Media", "https://www.hulu.com/series/frieren")
        )
    )

    MaterialTheme {
        Surface {
            AnimeDetailScreenContent(
                animeDetailState = UiState.Success(mockAnimeDetail), // Updated constructor
                animeEpisodesState = UiState.Loading(), // Updated constructor
                animeCharactersState = UiState.Loading(), // Updated constructor
                animeImagesState = UiState.Loading(), // Updated constructor
                modifier = Modifier.fillMaxSize(),
                animeVideosState = UiState.Loading(),
                animeReviewsState = UiState.Loading(),
                animeRecommendationState = UiState.Loading(),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun AnimeDetailScreenContentLoadingPreview() {
    MaterialTheme {
        Surface {
            AnimeDetailScreenContent(
                animeDetailState = UiState.Loading(), // Updated constructor
                animeEpisodesState = UiState.Loading(), // Updated constructor
                animeImagesState = UiState.Loading(), // Updated constructor
                animeCharactersState = UiState.Loading(), // Updated constructor
                animeReviewsState = UiState.Loading(),
                animeVideosState = UiState.Loading(),
                animeRecommendationState = UiState.Loading(),
                )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun AnimeDetailScreenContentErrorPreview() {
    MaterialTheme {
        Surface {
            AnimeDetailScreenContent(
                animeDetailState = UiState.Error(
                    "Failed to load anime details. Check your internet connection.",
                    
                ),
                animeEpisodesState = UiState.Error(
                    "Failed to load anime details. Check your internet connection.",
                    
                ),
                animeImagesState = UiState.Loading(),
                animeCharactersState = UiState.Loading(),
                animeReviewsState = UiState.Loading(),
                animeVideosState = UiState.Loading(),
                animeRecommendationState = UiState.Loading(),

                )
        }
    }
}