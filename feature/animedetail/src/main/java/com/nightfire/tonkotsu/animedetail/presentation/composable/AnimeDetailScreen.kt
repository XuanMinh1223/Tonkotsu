package com.nightfire.tonkotsu.animedetail.presentation.composable // Adjust your package as needed

import CharacterListSection
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.nightfire.tonkotsu.animedetail.presentation.AnimeDetailViewModel
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Character
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.News
import com.nightfire.tonkotsu.core.domain.model.Recommendation
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.core.domain.model.Video
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.ExpandableText
import com.nightfire.tonkotsu.ui.ImageList
import com.nightfire.tonkotsu.ui.TagSection
import com.nightfire.tonkotsu.ui.composables.AnimeEpisodesList
import com.nightfire.tonkotsu.ui.composables.VideoList
import com.nightfire.tonkotsu.ui.fullscreenoverlay.FullScreenOverlay
import com.nightfire.tonkotsu.ui.fullscreenoverlay.OverlayContent

@Composable
fun AnimeDetailScreen(
    malId: Int,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {

    val animeDetailState by viewModel.animeDetailState.collectAsState()
    val animeCharactersState by viewModel.animeCharactersState.collectAsState()
    val animeImagesState by viewModel.animeImagesState.collectAsState()
    val animeVideosState by viewModel.animeVideosState.collectAsState()
    val animeRecommendationState by viewModel.animeRecommendationsState.collectAsState()

    val animeEpisodes = viewModel.animeEpisodes.collectAsLazyPagingItems()
    val animeReviews = viewModel.animeReviews.collectAsLazyPagingItems()
    val animeNews = viewModel.animeNews.collectAsLazyPagingItems()

    LaunchedEffect(key1 = malId) {
        viewModel.getAnimeDetail(malId)
    }
    Scaffold(modifier = Modifier.systemBarsPadding()) { innerPadding ->
        AnimeDetailScreenContent(
            animeDetailState = animeDetailState,
            animeEpisodes = animeEpisodes,
            animeCharactersState = animeCharactersState,
            animeImagesState = animeImagesState,
            animeVideosState = animeVideosState,
            animeReviews = animeReviews,
            animeRecommendationState = animeRecommendationState,
            animeNews = animeNews,
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
    animeEpisodes: LazyPagingItems<AnimeEpisode>,
    animeCharactersState: UiState<List<Character>>,
    animeImagesState: UiState<List<Image>>,
    animeVideosState: UiState<List<Video>>,
    animeReviews: LazyPagingItems<AnimeReview>,
    animeRecommendationState: UiState<List<Recommendation>>,
    animeNews: LazyPagingItems<News>,
    modifier: Modifier = Modifier,
    onGenreClick: (String) -> Unit = {},
    onRecommendationClick: (Int) -> Unit = {},
    onRelationClick: (RelationEntry) -> Unit = {}
) {
    var overlayContent by remember  { mutableStateOf<OverlayContent?>(null) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = animeDetailState,
            label = "DetailTransition"
        ) { state ->
            when (state) { // Use 'when' with the sealed UiState
                is UiState.Loading -> {
                    AnimeDetailSkeletonScreen()
                }
                is UiState.Error -> {
                    ErrorCard(
                        message = state.message,
                        modifier = Modifier.padding(16.dp),
                        actionButtonText = "Retry",
                    )
                }
                is UiState.Success -> {
                    val anime = state.data
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
                                AnimeEpisodesList(animeEpisodes)
                            }
                            // Character List Section
                            CharacterListSection(animeCharactersState)
                            Spacer(Modifier.height(16.dp)) // Add spacing after character list
                            AppHorizontalDivider()
                            AnimeReviewList(
                                reviews = animeReviews,
                                onReviewClick = {
                                    overlayContent = OverlayContent.ReviewFullScreen(
                                        review = it
                                    )
                                }
                            )
                            AppHorizontalDivider()
                            AnimeNewsList(
                                news = animeNews
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
    }
    overlayContent?.let { content ->
        FullScreenOverlay(
            content = content,
            onDismiss = { overlayContent = null } // Dismisses the overlay by setting state to null
        )
    }
}
