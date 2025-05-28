package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A generic composable to display an error message, optionally with an action button.
 *
 * @param message The error message to display.
 * @param modifier The modifier to be applied to the root Column.
 * @param onActionClick An optional callback to be invoked when the action button is clicked.
 * If null, the action button will not be displayed.
 * @param actionButtonText The text to display on the action button. Defaults to "Retry".
 */
@Composable
fun ErrorCard(
    message: String,
    modifier: Modifier = Modifier,
    onActionClick: (() -> Unit)? = null,
    actionButtonText: String = "Retry"
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error icon",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(48.dp) // Larger icon
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            onActionClick?.let { onClick ->
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                    ) {
                    Text(actionButtonText)
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 240)
@Composable
fun ErrorCardPreview_NoAction() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ErrorCard(
                message = "Failed to load data. Please check your internet connection."
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 240)
@Composable
fun ErrorCardPreview_WithAction() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ErrorCard(
                message = "An unexpected error occurred. Do you want to refresh?",
                onActionClick = { /* Handle refresh logic in ViewModel */ },
                actionButtonText = "Refresh"
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 240)
@Composable
fun ErrorCardPreview_LongMessage() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ErrorCard(
                message = "This is a very long error message that might wrap to multiple lines to explain the situation in detail. It tries to give the user enough context to understand what went wrong. Please try again later.",
                onActionClick = { /* Handle retry logic */ },
                actionButtonText = "Try Again"
            )
        }
    }
}

// endregion Previews