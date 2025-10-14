package com.example.langchat.common.response

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

// @RestControllerAdvice: 모든 @RestController 에 대한 공통 작업을 수행함을 선언합니다.
@RestControllerAdvice
class CommonResponseAdvice : ResponseBodyAdvice<Any?> {

    // advice 적용 여부 (ture 반환시, 적용)
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return !StringHttpMessageConverter::class.java.isAssignableFrom(converterType)
    }

    /**
     * 응답 본문을 클라이언트에 보내기 직전 가로채는 메서드
     * 여기서는 응답 데이터를 조작(포장)할 수 있다.
     */
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if(body is ApiResponse<*>) {
            return body
        }

        return createApiResponse(
            code = "SUCCESS",
            message = "요청에 성공했습니다.",
            data = body
        )
    }
}