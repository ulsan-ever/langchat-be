package com.example.langchat.util

object IdGenerator {
    fun generateId(): String {
        // UUID를 사용하여 고유한 ID 생성
        return java.util.UUID.randomUUID().toString()
    }
}