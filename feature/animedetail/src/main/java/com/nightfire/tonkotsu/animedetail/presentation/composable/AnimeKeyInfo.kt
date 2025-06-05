package com.nightfire.tonkotsu.animedetail.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.nightfire.tonkotsu.core.domain.model.AnimeDetail
import com.nightfire.tonkotsu.ui.InfoRow
import java.time.LocalDate

@Composable
fun AnimeKeyInfo(anime: AnimeDetail) {
    Column {
        val today = LocalDate.now()
        InfoRow(label = "Type:", value = anime.type)
        InfoRow(label = "Episodes:", value = anime.episodes?.toString())
        InfoRow(label = "Status:", value = anime.status)
        anime.premiereDate?.let { premiereDate ->
            val premiereLabel = if (premiereDate.isAfter(today)) "Airs:" else "Premiered:"
            InfoRow(label = premiereLabel, value = premiereDate.toString())
        }
        anime.endDate?.let { endDate ->
            val endLabel = if (endDate.isAfter(today)) "Ends:" else "Ended:"

            InfoRow(label = endLabel, value = endDate.toString())
        }
        anime.broadcast?.let {
            InfoRow(label = "Broadcast time:", value = it)
        }
        InfoRow(label = "Duration:", value = anime.duration)
        InfoRow(label = "Rating:", value = anime.rating)
        InfoRow(label = "Source:", value = anime.source)
    }
}