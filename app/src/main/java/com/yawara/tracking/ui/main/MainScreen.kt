package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val bottomNavController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = { BottomNavBar(navController = bottomNavController) }
    ) { paddingValues ->
        BottomNavGraph(bottomNavController, paddingValues)
    }
}