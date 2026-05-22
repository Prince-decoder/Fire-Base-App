package com.example.chatapplication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapplication.Message.Message
import com.example.chatapplication.Message.MessageViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Group(
    navHostController: NavController,
    messageViewModel: MessageViewModel = viewModel(),
    name: String,
    id: String
) {
    val message by messageViewModel.message.observeAsState(emptyList())
    val currentUser by messageViewModel.currentUser.observeAsState(null)
    var mess by remember { mutableStateOf("") }

    LaunchedEffect(id) {
        Log.d("CHAT_DEBUG", "Group composable — id: '$id', name: '$name'")
        messageViewModel.setRoomId(id)
    }

    LaunchedEffect(message) {
        Log.d("CHAT_DEBUG", "Messages updated, count: ${message.size}")
        message.forEach { msg ->
            Log.d("CHAT_DEBUG", "Message: ${msg.text} from ${msg.senderFirstName}")
        }
    }

    LaunchedEffect(currentUser) {
        Log.d("CHAT_DEBUG", "Current user: ${currentUser?.firstName}")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                ),
                shadowElevation = 8.dp,
                color = colorResource(R.color.Appcolor)
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.Appcolor)),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = { navHostController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                            Text(name, fontWeight = FontWeight.Bold)
                        }
                    },
                )
            }
        },
        bottomBar = {
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
                            if (mess.isNotEmpty() && currentUser != null) {
                                messageViewModel.sendMessage(mess.trim())
                                mess = ""
                            } else if (currentUser == null) {
                                Log.e("CHAT_DEBUG", "Cannot send: currentUser is null")
                                messageViewModel.retryLoadUser()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (currentUser == null) {
            // Show loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Loading user data...")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(message) { msg ->
                    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                    chatMessageItem(
                        msg,
                        isowner = msg.senderId == currentUserEmail
                    )
                }
            }
        }
    }
}

@Composable
fun chatMessageItem(message: Message, isowner: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (isowner) {
            Alignment.End
        } else {
            Alignment.Start
        }
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isowner) {
                        colorResource(R.color.purple_500)
                    } else {
                        colorResource(R.color.teal_700)
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.senderFirstName,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}