package com.yawara.tracking.ui.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.yawara.tracking.ui.navigation.Screen
import androidx.navigation.compose.NavHost
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.ui.main.MainScreen
import kotlinx.coroutines.tasks.await


@Composable
fun AuthNavGraph(
    navigationController: NavHostController,
    paddingValues: PaddingValues,
) {
    val auth = FirebaseManager.auth
    var startDestination by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        if (auth.currentUser != null) {
            val userDoc =
                FirebaseManager.firestore.collection("users").document(auth.currentUser!!.uid).get()
                    .await()
            startDestination = if (userDoc.exists()) Screen.Dashboard.route else Screen.Auth.route
        } else {
            startDestination = Screen.Auth.route
        }
    }

    if (startDestination != null) {
        NavHost(
            navController = navigationController,
            startDestination = startDestination!!
        )
        {
            composable(Screen.Auth.route) {
                AuthScreen(
                    navigateToLogin = { navigationController.navigate(Screen.Login.route) },
                    navigateToSignUp = { navigationController.navigate(Screen.Register.route) })
            }
            composable(Screen.Login.route) {
                LoginScreen() {
                    navigationController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable(Screen.Register.route) {
                RegisterScreen() {
                    navigationController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            composable(Screen.Dashboard.route) { MainScreen(navController = navigationController) }
        }
    }

}