package com.yawara.tracking.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Dashboard : Screen("dashboard_screen", "Inicio", Icons.Default.Home)
    object CheckIn : Screen("checkin_screen", "Check In", Icons.Default.Check)
    object Posts : Screen("posts_screen", "???", Icons.Default.VideoLibrary)
    object Profile : Screen("profile_screen", "???", Icons.Default.Person)

    object Auth :Screen("auth_screen")
    object Login :Screen("login_screen")
    object Register :Screen("register_screen")

}