package com.nightfire.tonkotsu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val graphRoute: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavScreen("home_graph", "Home", Icons.Default.Home)
    object Search : BottomNavScreen("search_graph", "Search", Icons.Default.Search)
}