package com.example.chatapplication.User

import androidx.compose.ui.graphics.RectangleShape
import com.example.chatapplication.Results
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(val auth : FirebaseAuth,val firestore : FirebaseFirestore
    ) {

    suspend fun Signup(fname: String,lname:String,email:String,password:String): Results<Boolean>
    {
        return try {
            auth.createUserWithEmailAndPassword(email,password).await()
            val user = UserDetails(fname,lname,email,password)
            saveUserTOdatabase(user)
            Results.Success(true)
        }
        catch (e: Exception)
        {
            Results.error(e)
        }
    }

    suspend fun saveUserTOdatabase( userDetails: UserDetails): Results<Boolean>
    {
        return try {
            firestore.collection("Customer").document(userDetails.email)
                .set(userDetails).await()
            Results.Success(true)
        }catch (e: Exception)
        {
            Results.error(e)
        }
    }

    suspend fun Logout()
    {
        auth.signOut()
    }

    suspend fun signIn(email:String,pass: String): Results<Boolean>
    {
        return try {
            auth.signInWithEmailAndPassword(email,pass).await()
            Results.Success(true)
        }
        catch (e: Exception)
        {
            Results.error(e)
        }
    }

    suspend fun getCurrentUser(): Results<UserDetails>
    {
        return try {
            val email = auth.currentUser?.email?: return Results.error(Exception("User not present"))
            val snapshot = firestore.collection("Customer").document(email)
                .get()
                .await()
            val user = snapshot.toObject(UserDetails::class.java)
                ?:return Results.error(Exception("User not found"))
            Results.Success(user)
        }
        catch (e: Exception)
        {
            Results.error(e)
        }
    }

}