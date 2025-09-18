package com.yawara.tracking.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yawara.tracking.domain.usecase.Utils
import com.yawara.tracking.ui.navigation.Screen
import com.yawara.tracking.ui.theme.CustomTypography
import com.yawara.tracking.ui.viewmodel.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {

    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.userData?.name ?: "Tu perfil",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = CustomTypography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = ("Cinturón " + viewModel.userData?.rank + "   ~   " + Utils.parseDate(viewModel.userData?.lastRankUpdate)),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = CustomTypography.bodyLarge
            )
            Text(
                text = ("Miembro desde " + Utils.parseDate(viewModel.userData?.joinDate)),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = CustomTypography.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.closeSession()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = ButtonDefaults.squareShape
            ) {
                Text(text = "Cerrar sesión", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
