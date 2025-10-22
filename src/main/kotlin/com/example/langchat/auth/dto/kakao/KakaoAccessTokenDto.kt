package com.example.langchat.auth.dto.kakao

import jakarta.validation.constraints.NotEmpty

data class KakaoAccessTokenDto (
    @NotEmpty(message = "Access Token must not be empty")
    val accessToken: String
)