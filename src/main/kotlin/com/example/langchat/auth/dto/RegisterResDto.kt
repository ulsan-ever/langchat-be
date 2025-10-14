package com.example.langchat.auth.dto

data class RegisterResDto (
    val signUpToken: String,
    val name: String // validation 필요함
)