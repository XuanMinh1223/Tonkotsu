package com.nightfire.tonkotsu.animedetail.presentation.composable

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.nightfire.tonkotsu.core.domain.model.News
import androidx.core.net.toUri
import androidx.paging.LoadState
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.shimmerEffect
import java.time.format.DateTimeFormatter

@Composable
fun AnimeNewsRow(
    news: LazyPagingItems<News>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val loadState = news.loadState
    val rowHeight = 240.dp
    val cardWidth = 240.dp

    Column(modifier = modifier.fillMaxWidth()) {
        // Section title
        Text(
            text = "Latest News",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )

        when {
            // Initial loading state
            loadState.refresh is LoadState.Loading -> {
                NewsRowSkeleton(rowHeight = rowHeight, width = cardWidth)
            }

            // Error state — initial load failed
            loadState.refresh is LoadState.Error -> {
                val e = loadState.refresh as LoadState.Error
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorCard(
                        message = e.error.localizedMessage ?: "Unknown error",
                        modifier = Modifier.padding(16.dp),
                        actionButtonText = "Retry",
                        onActionClick = { news.retry() }
                    )
                }
            }

            // Empty state
            news.itemCount == 0 && loadState.refresh !is LoadState.Loading -> {
                Text(
                    text = "No news available at the moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                )
            }

            // Success state
            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(news.itemCount) { index ->
                        news[index]?.let { news ->
                            Card(
                                modifier = Modifier
                                    .width(cardWidth)
                                    .height(rowHeight) // FIXED height for all cards
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, news.url.toUri())
                                        context.startActivity(intent)
                                    },
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    if (!news.imageUrl.isNullOrBlank()) {
                                        AsyncImage(
                                            model = news.imageUrl,
                                            contentDescription = news.title,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Text(
                                        text = news.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = if (news.imageUrl.isNullOrBlank()) 5 else 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp)
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = news.authorUsername,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        news.date?.let { date ->
                                            val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
                                            val formattedDate = remember(date) { date.format(formatter) }
                                            Text(
                                                text = formattedDate,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsRowSkeleton(
    rowHeight: Dp,
    width: Dp = 240.dp
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(width)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect()
            )
        }
    }
}

