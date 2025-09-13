package com.example.langchat.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenReqDto (
    @get:JsonProperty("grant_type")
    val grantType: String = "authorization_code",

    @get:JsonProperty("client_id")
    val clientId: String,

    @get:JsonProperty("redirect_uri")
    val redirectUri: String,

    @get:JsonProperty("code")
    val code: String,

    @get:JsonProperty("client_secret")
    val clientSecret: String? = null // 선택적 파라미터
)