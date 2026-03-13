package com.example.chatapplication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogUp(navHostController: NavController,userViewModel: UserViewModel)
{

    var fname by remember { mutableStateOf("") }
    var lname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val result by userViewModel.authRESULT.observeAsState()

    Column(modifier = Modifier.fillMaxSize()
        .padding(8.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = fname, onValueChange = {fname=it}, label = {Text("First name") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = lname, onValueChange = {lname=it},label = {Text("last name") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = email, onValueChange = {email=it},label = {Text("email") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),value = password, onValueChange = {password=it},label = {Text("Password") }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier.fillMaxWidth(),onClick = {
            userViewModel.signUP(fname,lname,email,password)
        })
        {
            Text(text = "Register")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            Text("Already have and account ?")
            TextButton(onClick = {})
            {
                Text("Login ")
            }
        }
        when(result)
        {
            is Results.Loading -> {
                CircularProgressIndicator()
            }
            is Results.Success -> {
                fname=""
                lname=""
                email=""
                password=""
                LaunchedEffect(Unit) {
                    navHostController.navigate(Screens.LoginScreen.route)
                    {
                        popUpTo(Screens.LogUPScreen.route){inclusive=true}
                    }
                }
            }
            is Results.error -> {
                LaunchedEffect(result) {
                    Toast.makeText(
                        context, (result as Results.error).e.message?:"Unknown error", Toast.LENGTH_LONG).show()
                }
            }
            null->{}
        }
    }
}