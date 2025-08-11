package com.nightfire.tonkotsu.animedetail.presentation.composable

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.Recommendation
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import com.nightfire.tonkotsu.ui.ErrorCard
import com.nightfire.tonkotsu.ui.R
import com.nightfire.tonkotsu.ui.shimmerEffect

/**
 * A composable that displays a horizontal list of anime recommendations.
 *
 * @param uiState The UI state containing the list of [Recommendation]s (Loading, Success, or Error).
 * @param modifier Modifier for this composable.
 * @param onRecommendationClick Optional: Callback for when a recommendation card is clicked.
 */
@Composable
fun RecommendationList(
    uiState: UiState<List<Recommendation>>,
    modifier: Modifier = Modifier,
    onRecommendationClick: (Int) -> Unit = {} // Assuming malId for navigation
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppHorizontalDivider()
        Text(
            text = "Other Users Liked...",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        when (uiState) {
            is UiState.Loading -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5) { // Show 3 skeleton items (adjust count as needed for preview)
                        RecommendationCardSkeleton()
                    }
                }
            }
            is UiState.Success -> {
                val recommendations = uiState.data
                if (!recommendations.isEmpty())  {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recommendations) { recommendation ->
                            RecommendationCard(recommendation = recommendation, onClick = { malId ->
                                onRecommendationClick(malId)
                            })
                        }
                    }
                } else {
                    Text(
                        text = "No recommendations yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorCard( // Assuming ErrorCard is a composable you have
                        message = uiState.message,
                        modifier = Modifier.padding(16.dp),
                        actionButtonText = "Retry",
                        onActionClick = { /* ViewModel.retryFetchRecommendations() */ } // You'd pass a retry callback from ViewModel
                    )
                }
            }
        }
    }
}

/**
 * A composable card to display a single anime recommendation.
 *
 * @param recommendation The [Recommendation] domain model to display.
 * @param onClick Callback for when the card is clicked.
 */
@Composable
fun RecommendationCard(
    recommendation: Recommendation,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {} // Takes malId for navigation
) {
    Card(
        modifier = modifier
            .width(120.dp) // Fixed width for each card in the LazyRow
            .height(200.dp) // Fixed height to ensure consistency
            .clickable { onClick(recommendation.malId) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Recommendation Image
            AsyncImage(
                model = recommendation.imageUrl,
                contentDescription = "${recommendation.title} Recommendation Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // Fixed height for the image within the card
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop, // Crop to fill the image area
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.error_image)
            )

            // Spacer between image and text
            Spacer(Modifier.height(8.dp))

            // Recommendation Title
            Text(
                text = recommendation.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Votes (if available)
            recommendation.votes?.let { votes ->
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center, // Center votes horizontally
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp, // Using ThumbUp for votes
                        contentDescription = "Votes",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "$votes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(120.dp) // Match RecommendationCard width
            .height(200.dp), // Match RecommendationCard height
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // Match image height in RecommendationCard
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )

            Spacer(Modifier.height(8.dp))

            // Title placeholder
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(Modifier.height(4.dp))

            // Votes placeholder
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

// --- Previews ---

@Preview(showBackground = true, widthDp = 360, heightDp = 280)
@Composable
fun RecommendationListSuccessPreview() {
    MaterialTheme {
        Surface {
            val mockRecommendations = listOf(
                Recommendation(
                    malId = 1,
                    title = "Mushoku Tensei",
                    imageUrl = "https://cdn.myanimelist.net/images/anime/10/78745.jpg",
                    votes = 120
                ),
                Recommendation(
                    malId = 2,
                    title = "Re:Zero",
                    imageUrl = "https://cdn.myanimelist.net/images/anime/11/78750.jpg",
                    votes = 95
                ),
                Recommendation(
                    malId = 3,
                    title = "Konosuba",
                    imageUrl = "https://cdn.myanimelist.net/images/anime/12/78755.jpg",
                    votes = 80
                )
            )
            RecommendationList(uiState = UiState.Success(mockRecommendations))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 280)
@Composable
fun RecommendationListLoadingPreview() {
    MaterialTheme {
        Surface {
            RecommendationList(uiState = UiState.Loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 280)
@Composable
fun RecommendationListEmptyPreview() {
    MaterialTheme {
        Surface {
            RecommendationList(uiState = UiState.Success(emptyList()))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 280)
@Composable
fun RecommendationListErrorPreview() {
    MaterialTheme {
        Surface {
            RecommendationList(uiState = UiState.Error("Failed to load recommendations."))
        }
    }
}
