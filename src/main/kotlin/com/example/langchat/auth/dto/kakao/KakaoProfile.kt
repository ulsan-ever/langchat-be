package com.example.langchat.auth.dto.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoProfile (
    @JsonProperty("thumbnail_image_url")
    val thumbnailImageUrl: String,

    @JsonProperty("profile_image_url")
    val profileImageUrl: String,

    @JsonProperty("is_default_image")
    val isDefaultImage: Boolean
)