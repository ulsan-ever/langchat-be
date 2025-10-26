package com.example.langchat.common.response

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

// @RestControllerAdvice: 모든 @RestController 에 대한 공통 작업을 수행함을 선언.
@RestControllerAdvice
class CommonResponseAdvice : ResponseBodyAdvice<Any?> {

    // 문자열 응답에 대해서는 적용하지 않도록 설정
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return !StringHttpMessageConverter::class.java.isAssignableFrom(converterType)
    }

    // * 응답 본문을 클라이언트에 보내기 직전 가로채는 메서드
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        // 만약 body 가 이미 ApiResponse 형태라면, 그대로 반환 (ExceptionHandler 에서 먼저 감싼 경우 등)
        if(body is ApiResponse<*>) {
            return body
        }

        // 외의 응답건은 ApiResponse 로 감싸서 반환한다.
        return createApiResponse(
            code = "SUCCESS",
            message = "요청에 성공했습니다.",
            data = body
        )
    }
}