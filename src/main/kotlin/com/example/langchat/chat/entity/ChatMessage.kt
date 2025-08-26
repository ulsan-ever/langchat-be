package com.example.langchat.chat.entity

data class ChatMessage (
    val sender: String,
    val content : String,
    val roomId : String,
    val type: MessageType
)
