package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.core.domain.model.RelationEntry
import com.nightfire.tonkotsu.ui.AppHorizontalDivider
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun AnimeRelationsSection(anime: AnimeDetail, onRelationClick: (RelationEntry) -> Unit) {
    anime.relations.takeIf { it.isNotEmpty() }?.let { relationsMap ->
        AppHorizontalDivider()
        Spacer(Modifier.height(16.dp)) // Add spacing after divider
        Text(
            text = "Relations:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            relationsMap.forEach { (type, entries) ->
                Text(
                    text = "$type:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                entries.forEach { entry ->
                    // Make the entry name clickable
                    Text(
                        text = "• ${entry.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary, // Make text color distinct
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                onRelationClick(entry)
                            }
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
        }
        anime.externalLinks.takeIf { it.isNotEmpty() }?.let {
            Spacer(Modifier.height(16.dp)) // Add spacing before External Links divider
            AppHorizontalDivider()
            Spacer(Modifier.height(16.dp)) // Add spacing after divider
            Text(
                text = "External Links:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            ExternalUrlSection(navigableLinks = it, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
        }
        Spacer(Modifier.height(16.dp))
    }
}