package com.example.langchat.auth.dto.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoAccount (
    @JsonProperty("profile_image_needs_agreement")
    val profileImageNeedsAgreement: Boolean,

    val profile: KakaoProfile
)