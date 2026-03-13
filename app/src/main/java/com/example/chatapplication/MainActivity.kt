package com.example.chatapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.User.AuthState
import com.example.chatapplication.User.UserViewModel
import com.example.chatapplication.ui.theme.ChatApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navcontroller = rememberNavController()
            val userViewModel: UserViewModel= viewModel()
            ChatApplicationTheme {
                val authState by userViewModel.authstate.collectAsState()
                NavigationControl(navcontroller,userViewModel,authState)
            }
        }
    }
}


@Composable
fun NavigationControl(navHostController: NavHostController,userViewModel: UserViewModel,authState: AuthState)
{


    if (authState == AuthState.LOADING) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    val startDestination=if (userViewModel.isLoggedIn)
    {
        Screens.GroupsScreen.route
    }
    else
    {
        Screens.LoginScreen.route
    }

    NavHost(navController = navHostController,startDestination)
    {
        composable(Screens.LogUPScreen.route){
            LogUp(navHostController,userViewModel)
        }
        composable(Screens.LoginScreen.route){
            LogIn(navHostController,userViewModel)
        }
        composable(Screens.GroupsScreen.route){
            Groups(navHostController,userViewModel)
        }
        composable(Screens.MessageScreen.route){
            Group(navHostController,userViewModel)
        }
    }
}