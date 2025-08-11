package com.nightfire.tonkotsu.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

// You can define default shimmer colors or use MaterialTheme colors
val ShimmerColorShades = listOf(
    Color.LightGray.copy(0.9f),
    Color.LightGray.copy(0.2f),
    Color.LightGray.copy(0.9f)
)

fun Modifier.shimmerEffect(
    shape: Shape = RectangleShape, // Allows using different shapes like RoundedCornerShape
    shimmerColors: List<Color> = ShimmerColorShades,
    animationDuration: Int = 1200, // Duration for one shimmer cycle
    delay: Int = 200 // Delay before repeating
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // Adjust this value based on the max width/height you expect
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delay)
        ), label = "shimmerTranslate"
    )

    // Calculate dynamic brush for the gradient
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - 200f, y = translateAnimation.value - 200f),
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    this.then(
        Modifier
            .background(brush = brush, shape = shape)
    )
}