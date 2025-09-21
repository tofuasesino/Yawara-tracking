package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yawara.tracking.ui.theme.CustomTypography
import com.yawara.tracking.viewmodel.CheckInViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CheckInScreen(viewModel: CheckInViewModel = viewModel()) {

    val alreadyCheckIn by viewModel.alreadyCheckedIn

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Registro de asistencia",
            style = CustomTypography.displayLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.checkInUser() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = ButtonDefaults.squareShape,
            enabled = alreadyCheckIn == false
        ) {
            Text(
                if (alreadyCheckIn == true) "Check In" else "Check In",
                style = CustomTypography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (alreadyCheckIn == true) {
            Text(
                text = "¡Ya has hecho Check In hoy!",
                style = CustomTypography.bodyMedium,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }


}
