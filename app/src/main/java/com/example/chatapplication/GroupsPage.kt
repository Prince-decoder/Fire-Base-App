package com.example.chatapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.chatapplication.User.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Groups(navHostController: NavController,userViewModel: UserViewModel)
{
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope= rememberCoroutineScope()
    var show by remember { mutableStateOf(false) }

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start)
            {
                item{
                    Icon(Icons.Default.AccountCircle,"Image", modifier = Modifier.size(120.dp))
                    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = {}), verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start)
                    {
                        Icon(Icons.Default.AccountCircle,"Account")
                        Text("Profile")
                    }
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
            }
            ) {
            Column(modifier = Modifier.fillMaxSize()
                .padding(it))
            {

            }
        }
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
