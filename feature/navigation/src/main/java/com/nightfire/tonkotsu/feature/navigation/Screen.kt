package com.nightfire.tonkotsu.feature.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Defines all the navigation routes and their arguments in the application.
 * Each object represents a distinct screen or a navigation graph.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object AnimeDetail : Screen("anime_detail/{malId}") {
        fun createRoute(malId: Int) = "anime_detail/$malId"
    }
}