package com.yawara.tracking.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yawara.tracking.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CreatePostScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {

    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var videoUrl by rememberSaveable { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val author = viewModel.userName.value.toString()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {

            Text(
                text = "Crear post",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(5.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Cuerpo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("Link de video (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isEmpty() || content.isEmpty()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(
                                message = "El post necesita título y cuerpo."
                            )
                        }
                        return@Button
                    } else {
                        viewModel.createPost(title, content, videoUrl, author)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("post_created", true)

                        navController.popBackStack()
                    }


                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = ButtonDefaults.squareShape
            ) {
                Text("Publicar")
            }
        }
    }
}