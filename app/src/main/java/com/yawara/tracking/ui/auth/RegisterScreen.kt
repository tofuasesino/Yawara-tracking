package com.yawara.tracking.ui.auth

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.usecase.Utils

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RegisterScreen(navigateToHome: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var nameAndSurname by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Registrarse", style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = nameAndSurname,
            onValueChange = { nameAndSurname = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            label = { Text("Nombre y apellido") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            singleLine = true,
            label = { Text("Contraseña") },
            onValueChange = { password = it },
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
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
            onClick = { registerUser(email, password, nameAndSurname, navigateToHome) },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = ButtonDefaults.squareShape
        ) {
            Text("Registrarse", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

fun registerUser(
    email: String,
    password: String,
    nameAndSurname: String,
    navigateToHome: () -> Unit
) {
    val auth = FirebaseManager.auth


    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("UserSignUp", "Success.")
                createUserDoc(email, nameAndSurname)
                navigateToHome()
            }
        }
        .addOnFailureListener { Log.i("UserSignUp", "Failure.") }
}

fun createUserDoc(email: String, nameAndSurname: String) {
    val auth = FirebaseManager.auth

    val user = auth.currentUser
    user?.let {
        val uid = it.uid

        val userDoc = hashMapOf(
            "name" to nameAndSurname,
            "email" to email,
            "role" to "alumno",
            "joinDate" to Utils.getDateZeroed()
        )

        FirebaseManager.firestore.collection("users").document(uid).set(userDoc)
            .addOnSuccessListener {
                Log.i(
                    "UserSignUp",
                    "User created with ID successfully."
                )
            }
            .addOnFailureListener {
                Log.i(
                    "UserSignUp",
                    "User couldn't be bound with UID."
                )
            }
    }
}