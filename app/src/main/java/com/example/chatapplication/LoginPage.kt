package com.example.chatapplication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.User.UserViewModel

@Composable
fun LogIn(navHostController: NavController,userViewModel: UserViewModel)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val result by userViewModel.authRESULT.observeAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = email,
            onValueChange = {
                email=it
            }, label = { Text("Email")})
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password=it
            }, label = {
                Text("Password")
            }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.fillMaxWidth(),onClick = {

            userViewModel.signIN(email,password)
        }, enabled =result !is Results.Loading)
        {
            Text("Login")
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {
            Text("Don't have account ?")
            TextButton(onClick = {
                navHostController.navigate(Screens.LogUPScreen.route)
            }) { Text("Register")}
        }
        when(result)
        {
            is Results.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center)
                {
                    CircularProgressIndicator()
                }
            }
            is Results.Success -> {
                LaunchedEffect(Unit)
                {
                    navHostController.navigate(Screens.GroupsScreen.route)
                    {
                        popUpTo(Screens.LoginScreen.route){inclusive = true}
                    }
                }
            }
            is Results.error -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context,"Retry!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }
}
