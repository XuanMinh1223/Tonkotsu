package com.nightfire.tonkotsu.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation // For defining nested graphs
import com.nightfire.tonkotsu.animedetail.presentation.composable.AnimeDetailScreen
import com.nightfire.tonkotsu.feature.home.presentation.composable.HomeScreen
import com.nightfire.tonkotsu.navigation.Screen.AnimeDetailScreen
import com.nightfire.tonkotsu.navigation.Screen.HomeScreen

// Import the Composable functions from your feature modules

/**
 * Extension function to add the Anime Detail screen to a NavGraphBuilder.
 */
fun NavGraphBuilder.addAnimeDetailScreen() {
    composable(
        route = Screen.AnimeDetailScreen.route,
        arguments = Screen.AnimeDetailScreen.navArguments // Use the defined nav arguments
    ) { backStackEntry ->
        // Retrieve the argument for the AnimeDetailScreen
        val malId = backStackEntry.arguments?.getInt(Screen.Args.ANIME_ID)
            ?: throw IllegalStateException("Anime ID argument missing for AnimeDetailScreen")

        // Call the Composable from the animedetail feature module
        AnimeDetailScreen(malId = malId)
    }
}
/**
 * Extension function to add the Home feature's navigation graph.
 * This can contain multiple screens internal to the 'home' feature.
 * @param navController The NavController is passed if this graph needs to navigate
 * to destinations outside its immediate scope (e.g., to AnimeDetailScreen).
 */
fun NavGraphBuilder.addHomeGraph(navController: NavController) {
    // Defines a nested navigation graph for the 'home' feature
    navigation(
        startDestination = HomeScreen.route, // The first screen within the HomeGraph
        route = Screen.HomeGraph.route // The base route for the entire Home graph
    ) {
        // Define the HomeScreen Composable
        composable(HomeScreen.route) {
            // Call the Composable from the home feature module
            // We pass a lambda for navigation from HomeScreen to AnimeDetailScreen
            HomeScreen(
                onNavigateToAnimeDetail = { animeId ->
                    navController.navigate(AnimeDetailScreen.createRoute(animeId))
                }
            )
        }
        // Add other screens specific to the home feature if you add more later
        // composable(Screen.AnotherHomeScreen.route) { /* ... */ }
    }
}