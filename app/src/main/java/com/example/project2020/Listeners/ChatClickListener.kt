package com.example.project2020.Listeners

interface ChatClickListener {
    fun onChatClicked(name: String?, otherUserId: String?, chatImageUrl: String? , chatName: String?)
}