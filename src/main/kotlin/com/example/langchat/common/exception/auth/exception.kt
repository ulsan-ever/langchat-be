package com.example.langchat.common.exception.auth

class JwtValidationException (
    message: String = "토큰 정보가 유효하지 않습니다."
) : RuntimeException(message)

