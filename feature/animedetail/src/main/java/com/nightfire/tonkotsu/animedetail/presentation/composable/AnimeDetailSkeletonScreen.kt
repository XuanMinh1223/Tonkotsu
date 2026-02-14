package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.ui.shimmerEffect

@Composable
fun AnimeDetailSkeletonScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Title + Image
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 0) 0.7f else 0.5f)
                            .height(24.dp)
                            .padding(vertical = 4.dp)
                            .shimmerEffect(shape = RoundedCornerShape(4.dp))
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(225.dp)
                    .shimmerEffect(shape = RoundedCornerShape(8.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // Core Stats
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(vertical = 4.dp)
                    .shimmerEffect(shape = RoundedCornerShape(4.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // Watch Trailer button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .shimmerEffect(shape = RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(24.dp))

        // Key Info section (3–4 lines)
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(vertical = 4.dp)
                    .shimmerEffect(shape = RoundedCornerShape(4.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // Tag sections (genres/themes/categories)
        repeat(2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(28.dp)
                            .width(80.dp)
                            .shimmerEffect(shape = RoundedCornerShape(16.dp))
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ExpandableText blocks (Synopsis & Background)
        repeat(2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 8.dp)
                    .shimmerEffect(shape = RoundedCornerShape(6.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // VideoList + ImageList placeholders
        repeat(2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(3) {
                    Column {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .width(100.dp)
                                .height(16.dp)
                                .shimmerEffect(shape = RoundedCornerShape(4.dp))
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(14.dp)
                                .shimmerEffect(shape = RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        
        // Review & recommendation list placeholders
        repeat(2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 8.dp)
                    .shimmerEffect(shape = RoundedCornerShape(6.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // CharacterListSection placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // Adjust height as needed
                .padding(vertical = 8.dp)
                .shimmerEffect(shape = RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // AnimeNewsList placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Adjust height as needed
                .padding(vertical = 8.dp)
                .shimmerEffect(shape = RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // Streaming links section
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(24.dp)
                    .padding(vertical = 4.dp)
                    .shimmerEffect(shape = RoundedCornerShape(8.dp))
            )
        }

        Spacer(Modifier.height(24.dp))

        // AnimeThemesSection placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Adjust height as needed
                .padding(vertical = 8.dp)
                .shimmerEffect(shape = RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // AnimeRelationsSection placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Adjust height as needed
                .padding(vertical = 8.dp)
                .shimmerEffect(shape = RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))
    }
}
