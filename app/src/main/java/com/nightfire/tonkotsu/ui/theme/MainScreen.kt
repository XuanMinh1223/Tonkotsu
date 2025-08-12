package com.nightfire.tonkotsu.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nightfire.tonkotsu.BottomNavScreen
import com.nightfire.tonkotsu.animedetail.presentation.composable.AnimeDetailScreen
import com.nightfire.tonkotsu.feature.home.presentation.composable.HomeScreen
import com.nightfire.tonkotsu.feature.navigation.Screen
import com.nightfire.tonkotsu.feature.navigation.TonkotsuNavHost
import com.nightfire.tonkotsu.feature.search.presentation.SearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Search
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val currentTabRoute = when {
                    currentDestination?.hierarchy?.any { it.route == "home_graph" } == true -> "home_graph"
                    currentDestination?.hierarchy?.any { it.route == "search_graph" } == true -> "search_graph"
                    else -> "home_graph"
                }

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentTabRoute == screen.graphRoute,
                        onClick = {
                            navController.navigate(screen.graphRoute) {
                                popUpTo(screen.graphRoute) { inclusive = false }
                                launchSingleTop = true
                                restoreState = false // don't restore old backstack
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        TonkotsuNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            homeScreen = { onNavigate -> HomeScreen(onNavigate) },
            searchScreen = { onNavigate -> SearchScreen(onNavigate) },
            animeDetailScreen = { malId -> AnimeDetailScreen(malId) }
        )
    }
}

