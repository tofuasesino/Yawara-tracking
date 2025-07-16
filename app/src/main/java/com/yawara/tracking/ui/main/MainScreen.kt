package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yawara.tracking.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = { BottomNavBar(navController = bottomNavController) }
    ) { paddingValues ->
        BottomNavGraph(bottomNavController, mainNavController = navController, paddingValues = paddingValues)
    }
}