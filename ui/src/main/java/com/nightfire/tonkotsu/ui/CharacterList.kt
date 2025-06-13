package com.nightfire.tonkotsu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.Character
import androidx.compose.material3.Surface
import com.nightfire.tonkotsu.core.domain.model.VoiceActor // Nested VoiceActor

@Composable
fun CharacterListSection(
    uiState: UiState<List<Character>>, // Note: Assumes your ViewModel returns List<Character> not CharacterWithVoiceActors
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

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), // Height for the loading indicator in a horizontal list context
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "Failed to load characters.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        } else {
            val characters = uiState.data
            if (characters.isNullOrEmpty()) {
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
                    items(characters.size) { index ->
                        CharacterCard(character = characters[index], onClick = {
                            onCharacterClick(characters[index].malId)
                        })
                    }
                }
            }
        }
    }
}

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
                            "Atsumi Tanezaki",
                            "https://cdn.myanimelist.net/images/voiceactors/2/78028.jpg",
                            "Japanese"
                        )
                    ),
                    malId = 1,
                    name = "Frieren",
                    imageUrl = "https://cdn.myanimelist.net/images/characters/16/505391.jpg"
                ),
            )
            CharacterListSection(uiState = UiState.success(mockCharacters))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionLoadingPreview() {
    MaterialTheme {
        Surface {
            CharacterListSection(uiState = UiState.loading())
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionErrorPreview() {
    MaterialTheme {
        Surface {
            CharacterListSection(uiState = UiState.error("Failed to load characters."))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 250)
@Composable
fun CharacterListSectionEmptyPreview() {
    MaterialTheme {
        Surface {
            CharacterListSection(uiState = UiState.success(emptyList()))
        }
    }
}