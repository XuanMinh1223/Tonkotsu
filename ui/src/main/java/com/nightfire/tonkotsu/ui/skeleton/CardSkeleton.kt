package com.nightfire.tonkotsu.ui.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.nightfire.tonkotsu.ui.shimmerEffect

@Composable
fun CardSkeleton(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 100.dp,
    cardHeight: Dp = 180.dp,
    imageWidth: Dp = 90.dp,
    imageHeight: Dp = 120.dp,
    imageCornerRadius: Dp = 6.dp,
    cardCornerRadius: Dp = 8.dp
) {
    Box(
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight)
            .padding(4.dp)
            .clip(RoundedCornerShape(cardCornerRadius))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .shimmerEffect()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Skeleton for the image area
            Spacer(
                modifier = Modifier
                    .padding(4.dp)
                    .width(imageWidth)
                    .height(imageHeight)
                    .clip(RoundedCornerShape(imageCornerRadius))
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) // Base color beneath shimmer
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(imageWidth * 0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(imageWidth * 0.6f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    .shimmerEffect()
            )
        }
    }
}

@Preview
@Composable
fun CardSkeletonPreview() {
    MaterialTheme { // Use your app's theme here (e.g., TonkotsuTheme)
        Surface(color = MaterialTheme.colorScheme.background) {
            CardSkeleton()
        }
    }
}