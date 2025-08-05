package com.nightfire.tonkotsu.animedetail.presentation.composable // Or your appropriate UI package

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.composables.AnimeReviewItem
import com.nightfire.tonkotsu.ui.shimmerEffect

@Composable
fun AnimeReviewList(
    uiState: UiState<List<AnimeReview>>,
    modifier: Modifier = Modifier,
    rowHeight: Dp = 220.dp,
    onRetry: () -> Unit = {},
    onReviewClick: (AnimeReview, Int) -> Unit = { _, _ -> }
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // You might want a section title, similar to VideoList
        Text(
            text = "Reviews",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 8.dp) // Extra top padding if it's part of a larger screen
        )

        when (uiState) {
            is UiState.Loading -> {
                AnimeReviewListSkeleton(
                    rowHeight = rowHeight
                )
            }
            is UiState.Success -> {
                val reviews = uiState.data
                if (reviews.isEmpty()) {
                    Text(
                        text = "No reviews available for this anime yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                    )
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rowHeight),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(reviews) { index, review ->
                            AnimeReviewItem(
                                review = review,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable {
                                        onReviewClick(review, index)
                                    }
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorCard(
                        message = uiState.message, // 'message' is directly accessible
                        modifier = Modifier.padding(16.dp),
                        actionButtonText = "Retry",
                    )
                }
            }
        }
    }
}

@Composable
fun AnimeReviewListSkeleton(
    rowHeight: Dp,
    width: Dp =300.dp,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight), // Maintain height for consistent loading experience
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(width)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp)) // Match AnimeReviewItem corner radius
                    .shimmerEffect()
            )
        }
    }
}