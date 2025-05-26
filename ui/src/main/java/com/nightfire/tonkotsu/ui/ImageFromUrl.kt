package com.nightfire.tonkotsu.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text // Only for error text, can replace with icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
/**
 * A reusable Composable for loading images from URLs with common loading (shimmer)
 * and error (placeholder) states.
 *
 * @param imageUrl The URL of the image to load.
 * @param contentDescription The content description for accessibility.
 * @param modifier Modifier for the image. Defaults to a common image size.
 * @param width The desired width of the image. Defaults to 90.dp.
 * @param height The desired height of the image. Defaults to 120.dp.
 * @param contentScale The scale type for the image. Defaults to ContentScale.Crop.
 * @param errorDrawableRes The drawable resource ID for the error state. Defaults to R.drawable.error_image.
 * @param placeholderDrawableRes The drawable resource ID for the placeholder state. Defaults to R.drawable.placeholder_image.
 */
@Composable
fun ImageFromUrl(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    width: Dp = 90.dp,
    height: Dp = 120.dp,
    contentScale: ContentScale = ContentScale.Crop,
    errorDrawableRes: Int = R.drawable.error_image,
    placeholderDrawableRes: Int = R.drawable.placeholder_image,
    imageCornerRadius: Dp = 6.dp,
) {

    val errorTint = MaterialTheme.colorScheme.onErrorContainer
    val placeholderTint = MaterialTheme.colorScheme.onSurfaceVariant

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(imageCornerRadius)),
        contentScale = contentScale,
        error = { _ -> // The lambda receives AsyncImagePainter.State.Error, but we don't need it here, so use _
            Image(
                painter = painterResource(id = errorDrawableRes),
                contentDescription = null, // Or provide "Error loading image"
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(errorTint)
                )
        },
        loading = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Draw the placeholder image as a background for the shimmer
                Image(
                    painter = painterResource(id = placeholderDrawableRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop, // Or whatever scale you want for placeholder
                    colorFilter = ColorFilter.tint(placeholderTint) // Tint it here
                )
                // Then apply the shimmer effect on top or as an overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppImagePreview() {
    Column {
        Text("Loading State (Shimmer):")
        Surface(color = MaterialTheme.colorScheme.background) {
            ImageFromUrl(
                imageUrl = "", // Empty URL to force loading/placeholder
                contentDescription = "Loading image",
                modifier = Modifier.size(150.dp)
            )
        }
        Text("Loaded State:")
        Surface(color = MaterialTheme.colorScheme.background) {
            ImageFromUrl(
                imageUrl = "https://cdn.myanimelist.net/images/anime/10/78745.jpg", // A real URL
                contentDescription = "Loaded image",
                modifier = Modifier.size(150.dp)
            )
        }
        Text("Error State:")
        Surface(color = MaterialTheme.colorScheme.background) {
            ImageFromUrl(
                imageUrl = "https://invalid.url/nonexistent.jpg", // Invalid URL to force error
                contentDescription = "Error image",
                modifier = Modifier.size(150.dp)
            )
        }
    }
}