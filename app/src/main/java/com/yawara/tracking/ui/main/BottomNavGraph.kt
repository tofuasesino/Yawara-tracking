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
import com.yawara.tracking.viewmodel.AuthViewModel

@Composable
fun BottomNavGraph(
    navigationController: NavHostController,
    mainNavController: NavController,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navigationController,
        startDestination = Screen.Dashboard.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navigationController, authViewModel) }
        composable(Screen.CheckIn.route) { CheckInScreen() }
        composable(Screen.Posts.route) { PostsScreen() }
        composable(Screen.Profile.route) { ProfileScreen(mainNavController, authViewModel) }
        composable(Screen.CreatePostScreen.route) { CreatePostScreen(navigationController, authViewModel) }
    }
}