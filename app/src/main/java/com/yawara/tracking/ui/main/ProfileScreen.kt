package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yawara.tracking.ui.navigation.Screen
import com.yawara.tracking.ui.viewmodel.ProfileScreenViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenViewModel = viewModel()) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "???", modifier = Modifier.align(Alignment.Center))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.closeSession()
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cerrar sesión", style = MaterialTheme.typography.titleMedium)
        }
    }
}
