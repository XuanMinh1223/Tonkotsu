package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.Image

/**
 * A composable that displays a horizontal scrollable list of images.
 *
 * @param uiState The UI state containing the list of [Image]s (Loading, Success, or Error).
 * @param modifier Modifier for this composable.
 * @param onImageClick Callback for when an image is clicked, providing the [Image] and its [index].
 */
@Composable
fun ImageList(
    uiState: UiState<List<Image>>,
    modifier: Modifier = Modifier,
    onImageClick: (Image, Int) -> Unit = { _, _ -> } // <--- Updated callback signature
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Images",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Height for the loading indicator in a horizontal list context
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }
            is UiState.Success -> {
                val images = uiState.data
                if (images.isEmpty()) {
                    Text(
                        text = "No images available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(images) { index, image ->
                            AsyncImage(
                                model = image.url,
                                contentDescription = "Image ${index + 1}",
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(180.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable { onImageClick(image, index) }, // <--- Pass image and index
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.placeholder_image),
                                error = painterResource(id = R.drawable.error_image) // Optional: error image
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.isRetrying) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        }
                    } else {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Button(onClick = { /* ViewModel.retryFetchImages() */ }) { // You'd need a retry function in ViewModel
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}

// --- Previews (omitted for brevity, but you'd have them here) ---
@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun ImageListSuccessPreview() {
    MaterialTheme {
        Surface {
            val mockImages = listOf(
                Image(url = "https://cdn.myanimelist.net/images/anime/10/78745.jpg"),
                Image(url = "https://cdn.myanimelist.net/images/anime/11/78750.jpg"),
                Image(url = "https://cdn.myanimelist.net/images/anime/12/78755.jpg"),
            )
            ImageList(uiState = UiState.Success(mockImages))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun ImageListLoadingPreview() {
    MaterialTheme {
        Surface {
            ImageList(uiState = UiState.Loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun ImageListErrorPreview() {
    MaterialTheme {
        Surface {
            ImageList(uiState = UiState.Error("Failed to load images."))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 200)
@Composable
fun ImageListEmptyPreview() {
    MaterialTheme {
        Surface {
            ImageList(uiState = UiState.Success(emptyList()))
        }
    }
}

