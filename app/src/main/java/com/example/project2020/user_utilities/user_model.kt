package com.example.project2020.user_utilities

import android.provider.ContactsContract

data class User(
    val email: String? = "",
    val phone: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""
    )
data class Contact(
    val name: String?,
    val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)

data class Message(
    val sentBy: String? = "",
    val message: String? ="",
    val massageTime: Long? =0
)