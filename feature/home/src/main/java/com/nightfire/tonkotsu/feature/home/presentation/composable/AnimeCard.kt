package com.nightfire.tonkotsu.feature.home.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.ui.ImageFromUrl

/**
 * A composable to display a single Anime item card.
 * This is also stateless and takes its data as a parameter.
 */
@Composable
fun AnimeCard(anime: AnimeOverview, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(120.dp)
            .clickable {
                // TODO: Implement navigation to the detailed anime screen when clicked
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
        ) {
            ImageFromUrl(
                imageUrl = anime.imageUrl,
                contentDescription = "Image of ${anime.title}",
                width = 100.dp,
                height = 140.dp
            )

            Text(
                text = anime.title,
                style = MaterialTheme.typography.titleSmall, // A good size for card titles
                color = MaterialTheme.colorScheme.onSurface, // Text color on the card's surface
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // Add ellipsis for long titles
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
            )
            anime.score?.let {
                Text(
                    text = "Score: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // A slightly less prominent color for secondary info
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeCardPreview() {
    MaterialTheme { // Use your app's theme here
        Surface(color = MaterialTheme.colorScheme.background) {
            AnimeCard(
                anime = AnimeOverview(
                    malId = 100,
                    title = "My Hero Academia: World Heroes' Mission",
                    imageUrl = "https://cdn.myanimelist.net/images/anime/1769/117967.jpg",
                    score = 8.12,
                    type = "movie",
                    episodes = 1,
                    synopsis = "Under the doctrines of Quirk Doomsday Theory, the ideological group Humarise is convinced that all humans with quirks are diseased and must be eradicated. In order to rebuild the world, the group's extremists have constructed a lethal device known as a \"Trigger Bomb\" that causes people with quirks to lose control and die. Their leader, Flect Turn, evades capture from the Pro Heroes deployed around the world.\n" +
                            "\n" +
                            "During his work study in the country of Otheon with Japan's number one Pro Hero, Izuku \"Deku\" Midoriya is accused of a crime he did not commit. Unintentionally involving Roddy Soul, a local, Deku soon finds himself on the run with the boy. It is now up to Rody, Deku, and Deku's classmates to stop the Trigger Bomb plot set in motion by Flect, all while eluding the other persistent members of Humarise."
                )
            )
        }
    }
}