package com.example.langchat.user.dto

import com.example.langchat.user.entity.User

class RegisterResDto (
    val userId: Int,
    val email: String,
    val name: String
) {
    companion object {
        fun from(user : User) : RegisterResDto {
            return RegisterResDto(
                userId = user.userId,
                email = user.email,
                name = user.name
            )
        }
    }
}