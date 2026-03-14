package com.example.chatapplication.Room

import com.example.chatapplication.Results
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception


class RoomRepository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String): Results<Unit>
    {
        return try {
            val room= RoomData(name = name)
            firestore.collection("Rooms").add(room).await()
            Results.Success(Unit)
        }catch(e : Exception)
        {
            Results.error(e = e)
        }
    }
    suspend fun deleteRoom(roomId:String): Results<Unit>
    {
        return try {
            firestore.collection("Rooms").document(roomId)
                .delete()
                .await()
            Results.Success(Unit)
        }catch (e: Exception)
        {
            Results.error(e = e)
        }
    }
    fun getRoomRealtime(): Flow<Results<List<RoomData>>> = callbackFlow {
        val collection = firestore.collection("Rooms")
        val listener = collection.addSnapshotListener { snapshot,error ->
            if(error!= null)
            {
                trySend(Results.error(error))
                close()
                return@addSnapshotListener
            }
            if(snapshot!=null)
            {
                val rooms = snapshot.documents.map {
                    it.toObject(RoomData::class.java)!!.copy(id = it.id)
                }
                trySend(Results.Success(rooms))
            }
        }
        awaitClose {
            listener.remove()
        }
    }
}