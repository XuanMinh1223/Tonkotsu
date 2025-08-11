package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.ui.AppHorizontalDivider

@Composable
fun AnimeThemesSection(anime: AnimeDetail) {
    anime.openingThemes.takeIf { it.isNotEmpty() }?.let { themes ->
        AppHorizontalDivider()
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Opening Themes:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            themes.forEach { theme ->
                Text(text = "• $theme", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp)) // Add spacing after opening themes
    }

    anime.endingThemes.takeIf { it.isNotEmpty() }?.let { themes ->
        Text(
            text = "Ending Themes:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            themes.forEach { theme ->
                Text(text = "• $theme", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(16.dp)) // Add spacing after ending themes
    }
}
