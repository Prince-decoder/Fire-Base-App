package com.example.chatapplication

sealed class Results<out T> {
    data class Success<out T>(val data: T): Results<T>()
    data class error(val e : Exception): Results<Nothing>()
    object Loading : Results<Nothing>()
}