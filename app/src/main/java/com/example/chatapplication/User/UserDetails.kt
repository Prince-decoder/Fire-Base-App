package com.example.chatapplication.User

import com.google.firebase.firestore.PropertyName

data class UserDetails(
    @get:PropertyName("firstName") @set:PropertyName("firstName") var firstName: String = "",
    @get:PropertyName("lastName") @set:PropertyName("lastName") var lastName: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("password") @set:PropertyName("password") var password: String = ""
) {
    @get:PropertyName("FirstName") @set:PropertyName("FirstName")
    var legacyFirstName: String
        get() = firstName
        set(value) { if (value.isNotEmpty()) firstName = value }

    @get:PropertyName("LastName") @set:PropertyName("LastName")
    var legacyLastName: String
        get() = lastName
        set(value) { if (value.isNotEmpty()) lastName = value }

    @get:PropertyName("Email") @set:PropertyName("Email")
    var legacyEmail: String
        get() = email
        set(value) { if (value.isNotEmpty()) email = value }

    @get:PropertyName("Password") @set:PropertyName("Password")
    var legacyPassword: String
        get() = password
        set(value) { if (value.isNotEmpty()) password = value }
}