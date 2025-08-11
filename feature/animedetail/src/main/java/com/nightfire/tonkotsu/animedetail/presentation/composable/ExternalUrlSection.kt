package com.nightfire.tonkotsu.animedetail.presentation.composable

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.domain.model.NavigableLink
import androidx.core.net.toUri

@Composable
fun ExternalUrlSection(
    navigableLinks: List<NavigableLink>, modifier: Modifier) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        navigableLinks.forEach { service ->
            val context = LocalContext.current // Obtain the current Android Context
            Text(
                text = service.name, // Adjust if StreamingService has a different display field
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, service.url.toUri())
                        context.startActivity(intent)
                    }
            )
        }
    }
}