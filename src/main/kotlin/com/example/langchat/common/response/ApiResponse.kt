package com.example.langchat.common.response

data class ApiResponse<T>(
    val code: String,    // "SUCCESS", "SIGNUP_REQUIRED", "NOT_FOUND" 등
    val message: String, // 응답 관련 메시지
    val data: T?         // 실제 데이터
)

// 이제 success(), fail() 함수는 필요 없거나, ApiResponse 생성하는 팩토리 역할만 합니다.
fun <T> createApiResponse(code: String, message: String, data: T? = null): ApiResponse<T> {
    return ApiResponse(code, message, data)
}
