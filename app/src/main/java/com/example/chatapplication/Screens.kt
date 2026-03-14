package com.example.chatapplication

sealed class Screens(val route: String) {
    object LoginScreen: Screens("LoginPage")
    object LogUPScreen: Screens("LogUPPage")
    object GroupsScreen: Screens("GroupsPPage")
    object MessageScreen: Screens("MessagePage/{pageid}/{pagename}")
}