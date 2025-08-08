package com.nightfire.tonkotsu.ui.composables // Adjusted package for common composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.nightfire.tonkotsu.animedetail.presentation.composable.EpisodeListItem
import com.nightfire.tonkotsu.core.common.UiState // Import the sealed UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import com.nightfire.tonkotsu.ui.shimmerEffect
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun AnimeEpisodesList(
    animeEpisodes: LazyPagingItems<AnimeEpisode>, // Changed from AnimeEpisode to Episode
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Episodes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .shadow(2.dp, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                animeEpisodes.itemCount,
                animeEpisodes.itemKey { it.malId }
            ) { index ->
                val episode = animeEpisodes[index]
                if (episode != null) {
                    EpisodeListItem(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        episode = episode,
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            animeEpisodes.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { PagingLoadingItem() }
                    loadState.append is LoadState.Loading -> item { PagingLoadingItem() }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item { PagingErrorItem(e.error) }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        AppHorizontalDivider()
    }
}

@Composable
fun PagingLoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PagingErrorItem(error: Throwable?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Error: ${error?.message ?: "Unknown error"}")
    }
}