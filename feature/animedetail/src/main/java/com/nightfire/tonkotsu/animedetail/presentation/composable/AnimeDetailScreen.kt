package com.nightfire.tonkotsu.animedetail.presentation.composable

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.nightfire.tonkotsu.ui.composables.VideoList
import com.nightfire.tonkotsu.ui.fullscreenoverlay.FullScreenOverlay
import com.nightfire.tonkotsu.ui.fullscreenoverlay.OverlayContent

@Composable
fun AnimeDetailScreen(
    malId: Int,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {

    val animeDetailState by viewModel.animeDetailState.collectAsStateWithLifecycle()
    val animeCharactersState by viewModel.animeCharactersState.collectAsStateWithLifecycle()
    val animeImagesState by viewModel.animeImagesState.collectAsStateWithLifecycle()
    val animeVideosState by viewModel.animeVideosState.collectAsStateWithLifecycle()
    val animeRecommendationState by viewModel.animeRecommendationsState.collectAsStateWithLifecycle()

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

@OptIn(ExperimentalLayoutApi::class)
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
            when (state) {
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
                            AnimeCoreStats(anime = anime)
                            anime.trailerYoutubeId?.let { youtubeId ->
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        overlayContent = OverlayContent.VideoFullScreen(
                                            videos = listOf(Video(
                                                videoUrl = anime.trailerYoutubeUrl ?: "https://www.youtube.com/watch?v=$youtubeId",
                                                thumbnailUrl = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
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
                            AnimeKeyInfo(anime = anime)
                            Spacer(Modifier.height(16.dp))
                            AppHorizontalDivider()

                            TagSection(title = "Genres:", tags = anime.genres, onTagClick = onGenreClick)
                            TagSection(title = "Themes:", tags = anime.themes, onTagClick = onGenreClick)
                            TagSection(title = "Categories:", tags = anime.categories, onTagClick = onGenreClick)
                            Spacer(Modifier.height(16.dp))
                            AppHorizontalDivider()

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
                            AppHorizontalDivider()
                            TagSection(title = "Studios:", tags = anime.studios, isSecondary = true)
                            TagSection(title = "Producers:", tags = anime.producers, isSecondary = true)
                            TagSection(title = "Licensors:", tags = anime.licensors, isSecondary = true)
                            Spacer(Modifier.height(16.dp))

                            AppHorizontalDivider()
                            VideoList(
                                uiState = animeVideosState,
                                onVideoClick = { clickedVideo, index ->
                                    (animeVideosState as? UiState.Success)?.data?.let { videosList ->
                                        overlayContent = if (videosList.size == 1) {
                                            OverlayContent.VideoFullScreen(
                                                videos = listOf(clickedVideo),
                                                initialIndex = 0,
                                                title = anime.title
                                            )
                                        } else {
                                            OverlayContent.VideoFullScreen(
                                                videos = videosList,
                                                initialIndex = index,
                                                title = anime.title
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(16.dp))
                            AppHorizontalDivider()
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
                            Spacer(Modifier.height(16.dp))
                            AppHorizontalDivider()

                            if (anime.type?.lowercase() != "movie") {
                                AnimeEpisodesList(animeEpisodes)
                            }
                            CharacterListSection(animeCharactersState)
                            Spacer(Modifier.height(16.dp))
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

                            AnimeThemesSection(anime = anime)

                            AnimeRelationsSection(
                                anime = anime,
                                onRelationClick = onRelationClick,
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    overlayContent?.let { content ->
        FullScreenOverlay(
            content = content,
            onDismiss = { overlayContent = null }
        )
    }
}
