package com.example.langchat.auth.service

import com.example.langchat.auth.controller.KakaoUrl
import com.example.langchat.auth.dto.KakaoTokenResDto
import com.example.langchat.auth.dto.RedirectDto
import com.example.langchat.auth.dto.UnlinkResDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AuthService (
    private val webClient: WebClient,

    @Value("\${kakao.api-key}") // String Template 때문에 "\" 사용
    val apiKey: String

){
    suspend fun getKakaoToken(redirectDto: RedirectDto): KakaoTokenResDto {
        val formData = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code") // 고정 값
            add("client_id", apiKey)
            add("redirect_uri", KakaoUrl.REDIRECT_URL)
            add("code", redirectDto.code)
            // add("client_secret", "YOUR_CLIENT_SECRET") // 필요시 추가
        }

        return webClient.post()
            .uri(KakaoUrl.KAKAO_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .retrieve()
            .awaitBody<KakaoTokenResDto>()

    }

    suspend fun getUserInfo(accessToken: String): Map<String, Any> {
        return webClient.get()
            .uri(KakaoUrl.KAKAO_USER_INFO_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .awaitBody<Map<String, Any>>()
    }

    // 카카오 연결 해제 (개발 테스트 용도 API)
    suspend fun unlink(accessToken: String): UnlinkResDto {
        return webClient.post()
            .uri(KakaoUrl.UNLINK_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .awaitBody<UnlinkResDto>()
    }
}