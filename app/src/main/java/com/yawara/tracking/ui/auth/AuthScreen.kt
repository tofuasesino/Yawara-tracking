package com.yawara.tracking.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yawara.tracking.R
import com.yawara.tracking.ui.theme.Black
import com.yawara.tracking.ui.theme.White
import com.yawara.tracking.ui.theme.roboto

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun AuthScreen(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.yawara_icon),
            contentDescription = "gym logo",
            modifier = Modifier
                .clip(CircleShape)
                .size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dojo Yawara", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Siempre caminaremos juntos", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 32.dp),
            shape = ButtonDefaults.squareShape,
            border = BorderStroke(width = 1.dp, color = Black),
            onClick = { navigateToLogin() },
        ) { Text(text = "Iniciar sesión", style = MaterialTheme.typography.titleMedium) }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 32.dp),
            shape = ButtonDefaults.squareShape,
            border = BorderStroke(width = 1.dp, color = Black),
            onClick = { navigateToSignUp() }) {
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}