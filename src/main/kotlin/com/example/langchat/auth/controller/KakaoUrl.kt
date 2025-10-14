package com.example.langchat.auth.controller

object KakaoUrl {
    const val KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize"
    const val KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
    const val KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me"
    const val UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink"
    const val LOGIN_URL = "http://localhost:8080/api/kakao/login"
}