// feature/home/src/main/java/com/nightfire.tonkotsu.feature.home.presentation.composable/HomeScreen.kt
package com.nightfire.tonkotsu.feature.home.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // For injecting ViewModel
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview // Your domain model
import com.nightfire.tonkotsu.feature.home.presentation.HomeViewModel // Your ViewModel

/**
 * The Home screen composable function.
 * It observes the [HomeViewModel]'s state and displays the UI accordingly.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Title for the section
            Text(
                text = "Top Anime Shows",
                style = MaterialTheme.typography.headlineMedium, // Use a medium heading style
                modifier = Modifier.padding(bottom = 16.dp) // Add some space below the title
            )

            // Display different UI based on the state (Loading, Error, Success)
            if (state.isLoading) {
                // Show a loading indicator in the center
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    state.data?.let {
                        // Optionally show stale data while loading if available
                        if (it.isNotEmpty()) {
                            Text(text = "Loading more...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            } else if (state.errorMessage != null) {
                // Show an error message
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.errorMessage ?: "An unknown error occurred",
                        color = MaterialTheme.colorScheme.error, // Use error color for text
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Display the LazyRow when data is successfully loaded
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between items
                ) {
                    // Iterate over the list of anime overviews
                    items(state.data ?: emptyList()) { anime ->
                        // Placeholder for an individual anime card.
                        // For now, just display the title. We'll make a proper card later.
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp) // Padding for each item
                        ) {
                            // In a real app, you'd use Coil/Glide for image loading here
                            // For now, just a placeholder text for image
                            Text(
                                text = "Image: ${anime.imageUrl?.take(20)}...", // Truncate URL for display
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = anime.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2 // Limit title lines
                            )
                            anime.score?.let {
                                Text(text = "Score: $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Optional: A preview function for the HomeScreen
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    MaterialTheme {
        // You'd typically use a mock ViewModel here for actual previews
        // For simplicity, just showing the basic structure
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .systemBarsPadding()
            ) {
                Text(
                    text = "Top Anime Shows (Preview)",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listOf(
                        AnimeOverview(1, "Anime Title 1", "url1", 8.5, "TV", 12, "Synopsis 1"),
                        AnimeOverview(2, "Anime Title 2", "url2", 7.9, "Movie", 1, "Synopsis 2")
                    )) { anime ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Image Placeholder",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = anime.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2
                            )
                            anime.score?.let {
                                Text(text = "Score: $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}