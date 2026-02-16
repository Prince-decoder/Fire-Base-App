package com.example.chatapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Groups()
{
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope= rememberCoroutineScope()


    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {})
    {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(modifier = Modifier.fillMaxWidth(),title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text("Welcome")
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        })
                        {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp,"Logout")
                        }
                    }}, navigationIcon = {
                    IconButton(onClick = {}) {
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
}
