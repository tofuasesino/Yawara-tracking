package com.yawara.tracking.ui.main

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yawara.tracking.R
import com.yawara.tracking.data.datasource.FirebaseManager
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
        BottomNavGraph(
            bottomNavController,
            mainNavController = navController,
            paddingValues = paddingValues
        )
    }
}
