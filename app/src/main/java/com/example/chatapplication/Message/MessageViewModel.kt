package com.example.chatapplication.Message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Injection
import com.example.chatapplication.Results
import com.example.chatapplication.User.UserDetails
import com.example.chatapplication.User.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val messagerepo: MessageRepository
    private val userrepo: UserRepository

    init {
        messagerepo = MessageRepository(Injection.instance())
        userrepo = UserRepository(FirebaseAuth.getInstance(), Injection.instance())
        loadCurrentUser()

        // Listen for auth state changes
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            if (auth.currentUser != null) {
                Log.d("MessageViewModel", "Auth state changed - user logged in")
                loadCurrentUser()
            } else {
                Log.d("MessageViewModel", "Auth state changed - user logged out")
                _currentUser.value = null
                _message.value = emptyList()
            }
        }
    }

    private val _message = MutableLiveData<List<Message>>()
    val message: LiveData<List<Message>> = _message

    private val _roomId = MutableLiveData<String>()
    val roomId: LiveData<String> = _roomId

    private val _currentUser = MutableLiveData<UserDetails?>()
    val currentUser: LiveData<UserDetails?> = _currentUser

    private var messageJob: Job? = null

    fun sendMessage(text: String) {
        val user = _currentUser.value
        val roomIdValue = _roomId.value

        if (user == null) {
            Log.e("MessageViewModel", "Cannot send message: user is null")
            // Try to reload user
            loadCurrentUser()
            return
        }

        if (roomIdValue == null) {
            Log.e("MessageViewModel", "Cannot send message: roomId is null")
            return
        }

        val message = Message(
            senderFirstName = user.FirstName.ifEmpty {
                FirebaseAuth.getInstance().currentUser?.email?.split("@")?.first() ?: "User"
            },
            senderId = user.Email,
            text = text.trim()
        )

        viewModelScope.launch {
            try {
                Log.d("MessageViewModel", "Sending message: ${message.text} from ${message.senderFirstName}")
                messagerepo.sendMessage(roomIdValue, message)
                Log.d("MessageViewModel", "Message sent successfully")
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Error sending message: ${e.message}", e)
            }
        }
    }

    fun setRoomId(roomId: String) {
        Log.d("MessageViewModel", "Setting room ID: $roomId")
        _roomId.value = roomId
        loadMessages()
    }

    fun loadMessages() {
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            val roomIdValue = _roomId.value
            if (roomIdValue != null) {
                try {
                    Log.d("MessageViewModel", "Loading messages for room: $roomIdValue")
                    messagerepo.getChatMessage(roomIdValue)
                        .collect { messages ->
                            Log.d("MessageViewModel", "Received ${messages.size} messages")
                            _message.postValue(messages)
                        }
                } catch (e: Exception) {
                    Log.e("MessageViewModel", "Error loading messages: ${e.message}", e)
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                Log.d("MessageViewModel", "Loading current user...")
                val result = userrepo.getCurrentUser()
                when (result) {
                    is Results.Success -> {
                        Log.d("MessageViewModel", "User loaded successfully: ${result.data.FirstName}")
                        _currentUser.value = result.data
                    }
                    is Results.error -> {
                        Log.e("MessageViewModel", "Error loading user: ${result.e.message}")
                        // Try to get email from Firebase Auth as fallback
                        val authUser = FirebaseAuth.getInstance().currentUser
                        if (authUser != null) {
                            val fallbackUser = UserDetails(
                                FirstName = authUser.email?.split("@")?.first() ?: "User",
                                Email = authUser.email ?: "",
                                LastName = "",
                                Password = ""
                            )
                            _currentUser.value = fallbackUser
                            Log.d("MessageViewModel", "Using fallback user: ${fallbackUser.FirstName}")
                        }
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Exception loading user: ${e.message}", e)
            }
        }
    }

    fun retryLoadUser() {
        loadCurrentUser()
    }
}