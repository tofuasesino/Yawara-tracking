package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yawara.tracking.ui.navigation.Screen

@Composable
fun BottomNavGraph(
    navigationController: NavHostController,
    mainNavController: NavController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navigationController,
        startDestination = Screen.Dashboard.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navigationController) }
        composable(Screen.CheckIn.route) { CheckInScreen() }
        composable(Screen.Posts.route) { PostsScreen() }
        composable(Screen.Profile.route) { ProfileScreen(mainNavController) }
        composable(Screen.CreatePostScreen.route) { CreatePostScreen(navigationController) }
    }
}