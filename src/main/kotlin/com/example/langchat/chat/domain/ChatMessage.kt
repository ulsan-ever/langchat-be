package com.example.langchat.chat.domain

data class ChatMessage (
    val sender: String,
    val content : String,
    val type: MessageType
)
