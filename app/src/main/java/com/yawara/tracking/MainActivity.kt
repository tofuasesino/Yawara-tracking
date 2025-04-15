package com.yawara.tracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.yawara.tracking.ui.theme.YawaraTrackingAppTheme
import androidx.navigation.compose.rememberNavController
import com.yawara.tracking.ui.auth.AuthNavGraph


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()

            YawaraTrackingAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AuthNavGraph(navigationController, innerPadding)

                }

            }
        }
    }
}

//Scaffold(
//topBar = { MyTopAppBar() },
//bottomBar = { MyNavigationBar(navController) }
//) { innerPadding ->
//    NavHost(
//        navController = navigationController,
//        startDestination = Screen.Home.route,
//        modifier = Modifier.padding(innerPadding)
//    ) {
//        composable(Screen.Home.route) { HomeScreen() }
//        composable(Screen.CheckIn.route) { CheckInScreen() }
//        composable(Screen.Summary.route) { SummaryScreen() }
//        composable(Screen.Notes.route) { NotesScreen() }
//    }
//}





