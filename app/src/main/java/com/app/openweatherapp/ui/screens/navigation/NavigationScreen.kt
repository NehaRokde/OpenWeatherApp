package com.app.openweatherapp.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.openweatherapp.ui.screens.home.HomeScreen
import com.app.openweatherapp.ui.screens.home.HomeViewModel
import com.app.openweatherapp.ui.screens.search.SearchScreen

@Composable
fun NavigationView(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, homeViewModel)
        }
        composable("search") {
            SearchScreen(
                onHome = { navController.popBackStack() }
            )
        }
    }
}