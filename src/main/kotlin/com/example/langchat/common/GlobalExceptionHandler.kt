package com.example.langchat.common

import com.example.langchat.common.response.ApiResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClientResponseException

//@RestControllerAdvice
//class GlobalExceptionHandler {
//    @ExceptionHandler(WebClientResponseException::class)
//    fun handleWebClientResponseException(e : WebClientResponseException): ApiResponse<Nothing> {
//        return fail("EXTERNAL_API_ERROR", "외부 API 호출 중 오류가 발생했습니다: ${e.message}")
//    }
//}