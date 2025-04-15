package com.yawara.tracking.ui.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.ui.theme.Black
import com.yawara.tracking.ui.theme.Bombay
import com.yawara.tracking.ui.theme.Porcelain
import com.yawara.tracking.ui.theme.SilverChain
import com.yawara.tracking.ui.theme.White
import com.yawara.tracking.ui.theme.roboto


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(navigateToHome: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Iniciar sesión", style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().height(70.dp),
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            singleLine = true,
            label = { Text("Contraseña") },
            onValueChange = { password = it },
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().height(70.dp),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    val description = if (passwordHidden) "Ver contraseña" else "Ocultar contraseña"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                logInWithEmailAndPass(email, password, navigateToHome)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = ButtonDefaults.squareShape
        ) {
            Text("Entrar", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

fun logInWithEmailAndPass(email: String, password: String, navigateToHome: () -> Unit) {
    FirebaseManager.auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener { res ->
            Log.i("Login Firebase", "Estamos dentro: $res")
            navigateToHome()
        }.addOnFailureListener { res ->
            Log.i("Login Firebase", "FFFUUUCK: $res")
        }
}