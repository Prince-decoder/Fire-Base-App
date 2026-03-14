package com.example.chatapplication.Room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Injection
import com.example.chatapplication.Results
import kotlinx.coroutines.launch

class RoomViewModel: ViewModel() {

    private val _rooms = MutableLiveData<List<RoomData>>()
    val rooms: LiveData<List<RoomData>> get() = _rooms

    private val roomsRepository: RoomRepository
    init {
        roomsRepository = RoomRepository(
            Injection.instance())
            listenForRoomUpdates()
    }

    fun createRoom(name :String)
    {
        viewModelScope.launch {
            roomsRepository.createRoom(name = name)
        }
    }
    fun delete(id : String)
    {
        viewModelScope.launch {
            roomsRepository.deleteRoom(id)
        }
    }
    private fun listenForRoomUpdates()
    {
        viewModelScope.launch {
            roomsRepository.getRoomRealtime().collect {
                results ->
                when(results)
                {
                    is Results.Success ->
                    {
                        _rooms.value = results.data
                    }
                    is Results.error -> {}
                    is Results.Loading ->{}
                }
            }
        }
    }
}