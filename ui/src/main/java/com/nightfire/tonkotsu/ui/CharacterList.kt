import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Use items directly for simpler list iteration
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState // Import the sealed UiState
import com.nightfire.tonkotsu.core.domain.model.Character // Your Character domain model
import com.nightfire.tonkotsu.core.domain.model.VoiceActor // Nested VoiceActor
import androidx.compose.material3.Surface // For previews
import com.nightfire.tonkotsu.ui.CharacterCard
import com.nightfire.tonkotsu.ui.ErrorCard

@Composable
fun CharacterListSection(
    uiState: UiState<List<Character>>,
    modifier: Modifier = Modifier,
    onCharacterClick: (Int) -> Unit = {} // Assuming characterId for navigation
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Characters",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (uiState) { // Use 'when' with the sealed UiState
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Height for the loading indicator in a horizontal list context
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }
            is UiState.Success -> {
                val characters = uiState.data // 'data' is directly accessible and non-null here
                if (characters.isNullOrEmpty()) { // Check if the list itself is empty
                    Text(
                        text = "No characters available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 4.dp), // Padding around the entire row
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between cards
                    ) {
                        // Use 'items' directly with the list for simpler iteration
                        items(characters) { character ->
                            CharacterCard(character = character, onClick = {
                                onCharacterClick(character.malId) // Access malId directly from Character
                            })
                        }
                    }
                }
            }
            is UiState.Error -> {
                ErrorCard(
                    message = uiState.message, // 'message' is directly accessible
                    modifier = Modifier.padding(16.dp),
                    actionButtonText = "Retry",
                )
            }
        }
    }
}

// --- Previews ---

@Preview(showBackground = true, widthDp = 360, heightDp = 350)
@Composable
fun CharacterListSectionSuccessPreview() {
    MaterialTheme {
        Surface {
            val mockCharacters = listOf(
                Character(
                    role = "Main",
                    voiceActors = listOf(
                        VoiceActor(
                            101,
                            "Atsumi Tanezaki",
                            "https://cdn.myanimelist.net/images/voiceactors/2/78028.jpg",
                            "Japanese"
                        )
                    ),
                    malId = 1,
                    name = "Frieren",
                    imageUrl = "https://cdn.myanimelist.net/images/characters/16/505391.jpg"
                ),
                Character(
                    role = "Main",
                    voiceActors = listOf(
                        VoiceActor(
                            101,
                            "Kana Ichinose",
                            "https://cdn.myanimelist.net/images/voiceactors/2/78029.jpg",
                            "Japanese"
                        )
                    ),
                    malId = 2,
                    name = "Fern",
                    imageUrl = "https://cdn.myanimelist.net/images/characters/15/505392.jpg"
                ),
                Character(
                    role = "Supporting",
                    voiceActors = emptyList(),
                    malId = 3,
                    name = "Stark",
                    imageUrl = "https://cdn.myanimelist.net/images/characters/14/505393.jpg"
                ),
            )
            CharacterListSection(uiState = UiState.Success(mockCharacters)) // Updated constructor
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionLoadingPreview() {
    MaterialTheme {
        Surface {
            CharacterListSection(uiState = UiState.Loading()) // Updated constructor
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionErrorPreview() {
    MaterialTheme {
        Surface {
            // Updated to reflect the new UiState.Error constructor
            CharacterListSection(uiState = UiState.Error("Failed to load characters.", ))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionEmptyPreview() {
    MaterialTheme {
        Surface {
            CharacterListSection(uiState = UiState.Success(emptyList())) // Updated constructor
        }
    }
}
