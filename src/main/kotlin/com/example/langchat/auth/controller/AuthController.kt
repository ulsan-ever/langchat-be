package com.example.langchat.auth.controller

import com.example.langchat.auth.dto.KakaoTokenResDto
import com.example.langchat.auth.dto.RedirectDto
import com.example.langchat.auth.service.AuthService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api/kakao")
class AuthController (
    private val authService: AuthService,

    @Value("\${kakao.api-key}") // String Template 때문에 "\" 사용
    val apiKey: String

) {
    // 카카오 인가 코드 요청
    @GetMapping("/authorize")
    fun redirectToKakaoAuthorize(): ResponseEntity<Void> {
        val destinationUri = UriComponentsBuilder
            .fromUriString(KakaoUrl.KAKAO_AUTH_URL) // 1. 기본 URL 설정
            .queryParam("response_type", "code") // 2. 파라미터 추가 (고정 값)
            .queryParam("client_id", apiKey)
            .queryParam("redirect_uri", KakaoUrl.REDIRECT_URL)
            .encode() // 3. 파라미터 값을 URL 인코딩
            .build()
            .toUri() // 4. 최종적으로 URI 객체로 변환

        // 302 Redirect 응답 생성
        return ResponseEntity.status(HttpStatus.FOUND) // FOUND : 302 상태 코드
            .location(destinationUri)
            .build()
    }


    // 2. 카카오가 사용자를 이리로 돌려보냄 (인가 코드와 함께)
    @GetMapping("/redirect")
    suspend fun handleRedirect(@ModelAttribute redirectDto: RedirectDto): String {

        // TODO: 토큰 요청에 필요한 인가 코드를 받았는지 확인하고, 토큰 요청을 수행
        redirectDto.code?.let {
            val kakaoTokenResDto: KakaoTokenResDto = authService.getKakaoToken(redirectDto); // 인가 코드를 사용하여 토큰 요청
            val userInfoResDto = authService.getUserInfo(kakaoTokenResDto.accessToken)// 토큰 정보로 사용자 정보 조회
            println(userInfoResDto)

            return "Login Success! Got Authorization Code: $it"
        }

        // 인가 코드를 받지 못한 경우 (예: 사용자가 동의를 거부한 경우)
        println("No authorization code received")
        println(redirectDto)

        return "No authorization code received"
    }

}