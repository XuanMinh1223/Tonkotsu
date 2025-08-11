package com.nightfire.tonkotsu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nightfire.tonkotsu.feature.navigation.addAnimeDetailScreen
import com.nightfire.tonkotsu.feature.navigation.addHomeGraph
import com.nightfire.tonkotsu.feature.navigation.addSearchGraph

@Composable
fun TonkotsuNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route,
        modifier = modifier.fillMaxSize()
    ) {
        addHomeGraph(navController)
        addSearchGraph(navController)
        addAnimeDetailScreen()
    }
}