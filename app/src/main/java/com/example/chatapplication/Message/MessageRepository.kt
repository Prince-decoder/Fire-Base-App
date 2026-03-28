package com.example.chatapplication.Message

import android.util.Log
import com.example.chatapplication.Results
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository(val firestore: FirebaseFirestore) {

    suspend fun sendMessage(roomid: String, message: Message): Results<Unit> {
        return try {
            Log.d("MessageRepo", "Sending message to room: $roomid")
            firestore.collection("Rooms").document(roomid)
                .collection("messages").add(message).await()
            Log.d("MessageRepo", "Message sent successfully")
            Results.Success(Unit)
        } catch (e: Exception) {
            Log.e("MessageRepo", "Error sending message: ${e.message}", e)
            Results.error(e)
        }
    }

    fun getChatMessage(roomId: String): Flow<List<Message>> =
        callbackFlow {
            Log.d("MessageRepo", "Setting up listener for room: $roomId")
            val subscription = firestore.collection("Rooms").document(roomId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Log.e("MessageRepo", "Listen failed: ${error.message}")
                        close(error)
                        return@addSnapshotListener
                    }
                    querySnapshot?.let { snapshot ->
                        Log.d("MessageRepo", "Received ${snapshot.size()} messages")
                        val messages = snapshot.documents.mapNotNull { doc ->
                            try {
                                doc.toObject(Message::class.java)
                            } catch (e: Exception) {
                                Log.e("MessageRepo", "Error parsing message: ${e.message}")
                                null
                            }
                        }
                        trySend(messages).isSuccess
                    } ?: run {
                        trySend(emptyList()).isSuccess
                    }
                }
            awaitClose {
                Log.d("MessageRepo", "Listener removed")
                subscription.remove()
            }
        }
}