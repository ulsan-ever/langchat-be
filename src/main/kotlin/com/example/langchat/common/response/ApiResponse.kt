package com.example.langchat.common.response

data class ApiResponse<T>(
    val code: String,    // "SUCCESS", "SIGNUP_REQUIRED", "NOT_FOUND" 등
    val message: String, // 응답 관련 메시지
    val data: T?         // 실제 데이터
)

// ApiResponse 객체를 생성하는 메소드
fun <T> createApiResponse(code: String, message: String, data: T? = null): ApiResponse<T> {
    return ApiResponse(code, message, data)
}
