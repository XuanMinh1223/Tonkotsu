package com.nightfire.tonkotsu.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableText(title: String, text: String?) {
    if (text.isNullOrBlank()) return

    var expanded by remember { mutableStateOf(false) }
    // This state now tracks if the Text Composable, in its *current* configuration (maxLines),
    // is experiencing visual overflow. It will update regardless of `expanded`.
    var textOverflowsVisually by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize( // <--- It goes HERE!
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = if (expanded) Int.MAX_VALUE else 4, // Max lines depends on `expanded`
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    // Crucially: Update textOverflowsVisually always, not just when !expanded.
                    // When expanded is true, maxLines becomes Int.MAX_VALUE, so hasVisualOverflow
                    // will likely become false (unless text is truly massive).
                    // When expanded is false, maxLines is 4, and hasVisualOverflow tells us if we need "Read More".
                    textOverflowsVisually = textLayoutResult.hasVisualOverflow
                }
            )

            // Refined Button Display Logic:
            if (expanded) {
                TextButton(onClick = { expanded = false }) {
                    Text("Show Less")
                }
            } else if (textOverflowsVisually) {
                TextButton(onClick = { expanded = true }) {
                    Text("Read More")
                }
            }
        }
    }
}