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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nightfire.tonkotsu.BottomNavScreen
import com.nightfire.tonkotsu.animedetail.presentation.composable.AnimeDetailScreen
import com.nightfire.tonkotsu.feature.home.presentation.composable.HomeScreen
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
            NavigationBar  {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
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
