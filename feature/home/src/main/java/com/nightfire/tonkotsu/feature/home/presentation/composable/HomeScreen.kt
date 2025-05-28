// feature/home/src/main/java/com/nightfire.tonkotsu.feature.home.presentation.composable/HomeScreen.kt
package com.nightfire.tonkotsu.feature.home.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.nightfire.tonkotsu.core.common.UiState
import com.nightfire.tonkotsu.core.domain.model.AnimeOverview
import com.nightfire.tonkotsu.feature.home.presentation.HomeViewModel

/**
 * The Home screen composable function.
 * It observes the [HomeViewModel]'s state and displays the UI accordingly.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val popularAnimeState by viewModel.popularAnimeState.collectAsState()
    val topAiringAnimeState by viewModel.topAiringAnimeState.collectAsState()

    Scaffold(modifier = Modifier.systemBarsPadding()) { innerPadding ->
        HomeScreenContent(
            popularAnimeState = popularAnimeState,
            topAiringAnimeState = topAiringAnimeState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Stateless composable that displays the Home screen content based on the provided state.
 * This function is ideal for previews as it doesn't depend on a ViewModel.
 */
@Composable
fun HomeScreenContent(
    popularAnimeState: UiState<List<AnimeOverview>>,
    topAiringAnimeState: UiState<List<AnimeOverview>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()) // Use the passed modifier
    ) {
        AnimeRow(
            title = "Top Airing Anime",
            state = topAiringAnimeState
        )

        AnimeRow(
            title = "Most Popular Anime",
            state = popularAnimeState
        )
    }
}

// --- Preview Functions ---

// 1. Create a helper function to provide mock data
fun provideMockAnimeList(): List<AnimeOverview> {
    return listOf(
        AnimeOverview(
            malId = 1,
            title = "Shingeki no Kyojin",
            imageUrl = "https://cdn.myanimelist.net/images/anime/10/78745.jpg",
            score = 8.56,
            type = "tv",
            episodes = 25,
            synopsis = "Centuries ago, mankind was slaughtered to near extinction by monstrous humanoid creatures called Titans, forcing humans to hide in fear behind enormous concentric walls. What makes these giants truly terrifying is that their taste for human flesh is not born out of hunger but what appears to be out of pleasure. To ensure their survival, the remnants of humanity began living within defensive barriers, resulting in one hundred years without a single titan encounter. However, that fragile calm is soon shattered when a colossal Titan manages to breach the supposedly impregnable outer wall, reigniting the fight for survival against the man-eating abominations.\n" +
                    "\n" +
                    "After witnessing a horrific personal loss at the hands of the invading creatures, Eren Yeager dedicates his life to their eradication by enlisting into the Survey Corps, an elite military unit that combats the merciless humanoids outside the protection of the walls. Eren, his adopted sister Mikasa Ackerman, and his childhood friend Armin Arlert join the brutal war against the Titans and race to discover a way of defeating them before the last walls are breached."
        ),
        AnimeOverview(
            malId = 2,
            title = "Death Note",
            imageUrl = "https://cdn.myanimelist.net/images/anime/12/75421.jpg",
            score = 8.62,
            type = "tv",
            episodes = 37,
            synopsis = "Brutal murders, petty thefts, and senseless violence pollute the human world. In contrast, the realm of death gods is a humdrum, unchanging gambling den. The ingenious 17-year-old Japanese student Light Yagami and sadistic god of death Ryuk share one belief: their worlds are rotten.\n" +
                    "\n" +
                    "For his own amusement, Ryuk drops his Death Note into the human world. Light stumbles upon it, deeming the first of its rules ridiculous: the human whose name is written in this note shall die. However, the temptation is too great, and Light experiments by writing a felon's name, which disturbingly enacts his first murder.\n" +
                    "\n" +
                    "Aware of the terrifying godlike power that has fallen into his hands, Light—under the alias Kira—follows his wicked sense of justice with the ultimate goal of cleansing the world of all evil-doers. The meticulous mastermind detective L is already on his trail, but as Light's brilliance rivals L's, the grand chase for Kira turns into an intense battle of wits that can only end when one of them is dead.",
        ),
        AnimeOverview(
            malId = 3,
            title = "Fullmetal Alchemist: Brotherhood",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1223/96585.jpg",
            score = 9.10,
            type = "tv",
            episodes = 64,
            synopsis = "After a horrific alchemy experiment goes wrong in the Elric household, brothers Edward and Alphonse are left in a catastrophic new reality. Ignoring the alchemical principle banning human transmutation, the boys attempted to bring their recently deceased mother back to life. Instead, they suffered brutal personal loss: Alphonse's body disintegrated while Edward lost a leg and then sacrificed an arm to keep Alphonse's soul in the physical realm by binding it to a hulking suit of armor.\n" +
                    "\n" +
                    "The brothers are rescued by their neighbor Pinako Rockbell and her granddaughter Winry. Known as a bio-mechanical engineering prodigy, Winry creates prosthetic limbs for Edward by utilizing \"automail,\" a tough, versatile metal used in robots and combat armor. After years of training, the Elric brothers set off on a quest to restore their bodies by locating the Philosopher's Stone—a powerful gem that allows an alchemist to defy the traditional laws of Equivalent Exchange.\n" +
                    "\n" +
                    "As Edward becomes an infamous alchemist and gains the nickname \"Fullmetal,\" the boys' journey embroils them in a growing conspiracy that threatens the fate of the world."
        ),
        AnimeOverview(
            malId = 4,
            title = "One Punch Man",
            imageUrl = "https://cdn.myanimelist.net/images/anime/12/76059.jpg",
            score = 8.60,
            type = "tv",
            episodes =  12,
            synopsis = "The seemingly unimpressive Saitama has a rather unique hobby: being a hero. In order to pursue his childhood dream, Saitama relentlessly trained for three years, losing all of his hair in the process. Now, Saitama is so powerful, he can defeat any enemy with just one punch. However, having no one capable of matching his strength has led Saitama to an unexpected problem—he is no longer able to enjoy the thrill of battling and has become quite bored.\n" +
                    "\n" +
                    "One day, Saitama catches the attention of 19-year-old cyborg Genos, who witnesses his power and wishes to become Saitama's disciple. Genos proposes that the two join the Hero Association in order to become certified heroes that will be recognized for their positive contributions to society. Saitama, who is shocked that no one knows who he is, quickly agrees. Meeting new allies and taking on new foes, Saitama embarks on a new journey as a member of the Hero Association to experience the excitement of battle he once felt."
        )
    )
}

// Assuming your HomeUiState is defined like this (or similar in HomeViewModel.kt):
// data class HomeUiState(
//     val data: List<AnimeOverview>? = null, // Renamed from topAnimeList to data
//     val isLoading: Boolean = false,
//     val errorMessage: String? = null
// )

@Preview(showBackground = true)
@Composable
fun HomeScreenContentSuccessPreview() {
    MaterialTheme { // Use your app's theme here (e.g., TonkotsuTheme)
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenContent(
                popularAnimeState = UiState(
                    data = provideMockAnimeList(),
                    isLoading = false,
                    errorMessage = null
                ),
                topAiringAnimeState = UiState(
                    data = provideMockAnimeList(),
                    isLoading = false,
                    errorMessage = null
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentLoadingPreview() {
    MaterialTheme { // Use your app's theme here
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenContent(
                popularAnimeState = UiState(
                    data = if (provideMockAnimeList().isNotEmpty()) provideMockAnimeList() else null, // Show stale data if desired
                    isLoading = true,
                    errorMessage = null
                ),
                topAiringAnimeState = UiState(
                    data = if (provideMockAnimeList().isNotEmpty()) provideMockAnimeList() else null, // Show stale data if desired
                    isLoading = true,
                    errorMessage = null
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentErrorPreview() {
    MaterialTheme { // Use your app's theme here
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenContent(
                popularAnimeState = UiState(
                    data = null,
                    isLoading = false,
                    errorMessage = "Failed to load anime. Please try again."
                ),
                topAiringAnimeState = UiState(
                    data = if (provideMockAnimeList().isNotEmpty()) provideMockAnimeList() else null, // Show stale data if desired
                    isLoading = true,
                    errorMessage = null
                )
            )
        }
    }
}
