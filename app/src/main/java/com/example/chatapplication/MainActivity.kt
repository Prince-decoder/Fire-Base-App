package com.example.chatapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapplication.Room.RoomViewModel
import com.example.chatapplication.User.AuthState
import com.example.chatapplication.User.UserViewModel
import com.example.chatapplication.ui.theme.ChatApplicationTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navcontroller = rememberNavController()
            val userViewModel: UserViewModel= viewModel()
            val roomViewModel: RoomViewModel = viewModel()
            ChatApplicationTheme {
                val authState by userViewModel.authstate.collectAsState()
                NavigationControl(navcontroller,userViewModel,roomViewModel,authState)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationControl(navHostController: NavHostController,userViewModel: UserViewModel,roomViewModel: RoomViewModel,authState: AuthState)
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
            Groups(navHostController,userViewModel,roomViewModel)
        }
        composable(Screens.MessageScreen.route,
            arguments = listOf(
                navArgument("pageid"){type = NavType.StringType},
                navArgument("pagename"){type = NavType.StringType}
            )){
            val nam = it.arguments?.getString("pagename")?:""
            val id = it.arguments?.getString("pageid")?:""
            Group(navHostController,name = nam,id =id)
        }
    }
}