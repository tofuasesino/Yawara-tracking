package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yawara.tracking.data.repository.UserRepository
import com.yawara.tracking.ui.viewmodel.AuthViewModel

@Composable
fun MainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(UserRepository())
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { MyTopAppBar() },
        bottomBar = { BottomNavBar(navController = bottomNavController) }
    ) { paddingValues ->
        BottomNavGraph(
            bottomNavController,
            mainNavController = navController,
            paddingValues = paddingValues,
            authViewModel
        )
    }
}
