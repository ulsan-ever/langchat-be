package com.example.langchat.chat.domain

enum class MessageType {
    CHAT, // 일반 채팅 메시지
    JOIN, // 참여 메시지
    LEAVE // 퇴장 메시지
}