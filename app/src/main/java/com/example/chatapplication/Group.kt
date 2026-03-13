package com.example.chatapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chatapplication.User.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Group(navHostController: NavController,userViewModel: UserViewModel)
{
    var mess by remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                ),
                shadowElevation = 8.dp,
                color = colorResource(R.color.Appcolor)
            ){
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.Appcolor)),
                title = {
                Row(modifier = Modifier.fillMaxWidth()
                , verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Text("heko",
                        fontStyle = FontStyle.Italic,
                        )

                IconButton(onClick = {})
             {
                 Icon(Icons.Default.Delete,"Delete Group")
             }}},
                )
        }}, bottomBar = {
            OutlinedTextField(
                value = mess,
                onValueChange = { mess = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Type a message") },
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // Send logic
                            mess = ""
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )
        }) {
        Column(modifier = Modifier.fillMaxSize().padding(it),
            ) {  } }
}