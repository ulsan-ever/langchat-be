package com.example.langchat.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenResDto (
    @JsonProperty("token_type")
    val tokenType: String, // "bearer"로 고정

    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("expires_in")
    val expiresIn: Int,

    @JsonProperty("refresh_token")
    val refreshToken: String,

    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int,

    // 사용자가 동의한 권한 범위 (e.g., "profile_nickname profile_image")
    // 응답에 포함되지 않을 수도 있으므로 nullable로 선언
    @JsonProperty("scope")
    val scope: String?,

    // OpenID Connect를 사용하는 경우에만 포함되는 ID 토큰
    // 응답에 포함되지 않을 수도 있으므로 nullable로 선언
    @JsonProperty("id_token")
    val idToken: String?
)