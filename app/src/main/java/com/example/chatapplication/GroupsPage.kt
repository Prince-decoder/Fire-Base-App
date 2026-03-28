package com.example.chatapplication

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.chatapplication.Room.RoomData
import com.example.chatapplication.Room.RoomViewModel
import com.example.chatapplication.User.UserViewModel
import kotlinx.coroutines.launch
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Groups(navHostController: NavController,userViewModel: UserViewModel,Roomsview: RoomViewModel)
{
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope= rememberCoroutineScope()
    var show by remember { mutableStateOf(false) }
    var showAdd by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    val rooms by Roomsview.rooms.observeAsState(initial = emptyList())

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start)
            {
                item(){

                }
            }
        }
        })
    {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(modifier = Modifier.fillMaxWidth(),title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text("Welcome")
                        IconButton(onClick = {
                            show= true
                        })
                        {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp,"Logout")
                        }
                    }}, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Default.Menu,"Menu")
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.Appcolor)))
            }, floatingActionButton = {
                FloatingActionButton(onClick = {showAdd= true})
                {
                    Icon(Icons.Default.Add,"Add Room")
                }
            }
            ) {
            Column(modifier = Modifier.fillMaxSize()
                .padding(it))
            {
LazyColumn(modifier = Modifier.fillMaxWidth().padding(10.dp))
{
    items(rooms){
        RoomsV(it,navHostController)
    }
}
            }
        }
    }
    if(showAdd)
    {
        AlertDialog(onDismissRequest = {showAdd= false}, confirmButton = {},
            text =
            {
                Column(modifier = Modifier.fillMaxWidth().padding(10.dp))
                {
                    OutlinedTextField(value = name,
                        onValueChange = {
                            name = it
                        }, label = {Text("Name")})

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Button(onClick = {
                        scope.launch {
                            if (name.isNotBlank())
                            {
                                showAdd= false
                                Roomsview.createRoom(name)
                            }
                            else
                            {
                                Toast.makeText(context,"Enter the name", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, modifier = Modifier.wrapContentSize())
                    {
                        Text("Create")
                    }
                    Button(onClick = {showAdd=false}, modifier = Modifier.wrapContentSize()) {
                        Text("Cancel")
                    }
                }
            }})
    }

    if(show)
    {
        AlertDialog(onDismissRequest = {}, confirmButton = {}, text = {
            Column(Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Are you sure ?")
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Button(onClick = {
                        show = false
                        userViewModel.logOUT()
                    }) {
                        Text("Yes")
                    }
                    Button(onClick = {
                        show=false
                    }) {
                        Text("No")
                    }
                }
            }
        }, title = {Text("Alert")})
    }
}
@Composable
fun RoomsV(room: RoomData,navController: NavController)
{
    Column(modifier = Modifier.fillMaxSize())
    {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = room.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.Appcolor)
            )
            Button(onClick = {
                navController.navigate("MessagePage/${room.id}/${room.name}")
            }, modifier = Modifier.wrapContentSize())
            {
                Text("Join")
            }
        }
        Divider()
    }
}