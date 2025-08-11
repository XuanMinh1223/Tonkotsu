package com.nightfire.tonkotsu.feature.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Defines all the navigation routes and their arguments in the application.
 * Each object represents a distinct screen or a navigation graph.
 */
sealed class Screen(
    val route: String, // The base route string for the screen or graph
    val navArguments: List<NamedNavArgument> = emptyList() // Optional arguments
) {
    // --- Top-Level Graphs / Features ---
    object HomeGraph : Screen("home_graph")
    object SearchGraph : Screen("search_graph")

    // --- Individual Screens within Features ---

    object HomeScreen : Screen("home_screen")

    // Anime Detail Screen
    object AnimeDetailScreen : Screen(
        route = "anime_detail_screen/{${Args.ANIME_ID}}",
        navArguments = listOf(
            navArgument(Args.ANIME_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        // Helper function to create the route with the actual anime ID
        fun createRoute(animeId: Int) = "anime_detail_screen/$animeId"
    }

    object Search : Screen("search")

    // --- Internal Constants for Navigation Arguments ---
    object Args {
        const val ANIME_ID = "malId"
    }
}