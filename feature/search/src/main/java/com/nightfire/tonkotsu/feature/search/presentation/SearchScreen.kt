package com.nightfire.tonkotsu.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.feature.search.SearchViewModel
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import com.nightfire.tonkotsu.ui.shimmerEffect
import java.util.Locale

@Composable
fun SearchScreen(
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val currentQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Local text state to capture user typing without triggering search yet
    var textFieldValue by remember { mutableStateOf(currentQuery.query ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search anime...") },
            leadingIcon = {
                IconButton(onClick = {
                    // Trigger search on icon click
                    viewModel.updateSearchQuery(currentQuery.copy(query = textFieldValue.ifBlank { null }))
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.updateSearchQuery(currentQuery.copy(query = textFieldValue.ifBlank { null }))
                }
            )
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(searchResults.itemCount) { index ->
                val anime = searchResults[index]
                anime?.let {
                    AnimeSearchListItem(
                        anime = anime,
                        onClick = onNavigateToAnimeDetail
                        )
                }
                if (index < searchResults.itemCount - 1) {
                    AppHorizontalDivider()
                }
            }

            searchResults.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        items(5) { AnimeSearchListItemPlaceholder() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            AnimeSearchListItemPlaceholder()
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            Text("Error: ${e.error.localizedMessage}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeSearchListItem(
    anime: AnimeOverview,
    onClick: (Int) -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(anime.malId) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(anime.malId) }
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Poster image
            anime.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = anime.title,
                    modifier = Modifier
                        .size(width = 80.dp, height = 110.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Type + Episodes
                val typeAndEpisodes = listOfNotNull(
                    anime.type,
                    anime.episodes?.let { "$it ep" }
                ).joinToString(" • ")

                if (typeAndEpisodes.isNotEmpty()) {
                    Text(
                        text = typeAndEpisodes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Score row
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Score",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = anime.score?.let { String.format(Locale.US, "%.2f", it) } ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Synopsis snippet
                anime.synopsis?.takeIf { it.isNotBlank() }?.let { synopsis ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = synopsis,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AnimeSearchListItemPlaceholder() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Poster skeleton
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 110.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Type + Episodes skeleton
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(14.dp)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Score row skeleton
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(14.dp)
                        .shimmerEffect()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Synopsis snippet skeleton
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .padding(vertical = 2.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}