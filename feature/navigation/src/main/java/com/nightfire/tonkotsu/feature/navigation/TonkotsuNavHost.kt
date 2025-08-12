package com.nightfire.tonkotsu.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun TonkotsuNavHost(
    navController: NavHostController,
    homeScreen: @Composable (onNavigateToAnimeDetail: (Int) -> Unit) -> Unit,
    searchScreen: @Composable (onNavigateToAnimeDetail: (Int) -> Unit) -> Unit,
    animeDetailScreen: @Composable (malId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            homeScreen { malId ->
                navController.navigate(Screen.AnimeDetail.createRoute(malId))
            }
        }
        composable(Screen.Search.route) {
            searchScreen { malId ->
                navController.navigate(Screen.AnimeDetail.createRoute(malId))
            }
        }
        composable(
            route = Screen.AnimeDetail.route,
            arguments = listOf(navArgument("malId") { type = NavType.IntType })
        ) { backStackEntry ->
            val malId = backStackEntry.arguments?.getInt("malId") ?: error("Missing malId")
            animeDetailScreen(malId)
        }
    }
}