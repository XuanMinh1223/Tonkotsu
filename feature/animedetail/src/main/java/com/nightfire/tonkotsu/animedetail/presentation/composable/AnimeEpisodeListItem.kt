package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeEpisode
import java.util.Locale

@Composable
fun EpisodeListItem(
    modifier: Modifier,
    episode: AnimeEpisode,
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 0.dp) // Item specific padding
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Episode number and title
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${episode.malId}. ${episode.title}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                episode.titleJapanese?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                episode.titleRomanji?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Episode score (if available)
            episode.score?.let {
                Text(
                    text = String.format(Locale.US, "%.1f", it), // Format score to one decimal place
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Use primary color for score
                )
            }
        }

        // Aired date
        episode.airedDate?.let { date ->
            Text(
                text = "Aired: ${date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Filler/Recap status (if true)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (episode.isFiller) {
                Text(
                    text = "Filler",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.small)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            if (episode.isRecap) {
                Text(
                    text = "Recap",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}