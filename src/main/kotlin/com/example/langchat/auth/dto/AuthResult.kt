package com.example.langchat.auth.dto

import com.example.langchat.auth.dto.kakao.KaKaoUserInfoRes

sealed class AuthResult {

    // 로그인 성공 상태
    data class LoginSuccess(
        val accessToken: String,
        val refreshToken: String,
        val kaKaoUserInfoRes: KaKaoUserInfoRes?
    ) : AuthResult()

    // 회원가입 필요 상태
    data class SignupRequired(
        val signupToken: String
    ) : AuthResult()
}