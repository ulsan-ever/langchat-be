package com.example.langchat.auth.service

import com.example.langchat.auth.controller.KakaoUrl
import com.example.langchat.auth.dto.kakao.KaKaoUserInfoRes
import com.example.langchat.auth.dto.kakao.KakaoTokenResDto
import com.example.langchat.auth.dto.RedirectDto
import com.example.langchat.auth.dto.RegisterResDto
import com.example.langchat.auth.dto.UnlinkResDto
import com.example.langchat.auth.entity.User
import com.example.langchat.auth.repository.AuthRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class AuthService (
    private val authRepository: AuthRepository,

    private val webClient: WebClient,

    @Value("\${kakao.api-key}") // String Template 때문에 "\" 사용
    val apiKey: String,

    @Value("\${kakao.admin-key}")
    val adminKey: String

){

    fun makeAuthorizeUrl () : URI {
        return UriComponentsBuilder
            .fromUriString(KakaoUrl.KAKAO_AUTH_URL) // 1. 기본 URL 설정
            .queryParam("response_type", "code") // 2. 파라미터 추가 (고정 값)
            .queryParam("client_id", apiKey)
            .queryParam("redirect_uri", KakaoUrl.LOGIN_URL) // 리다이렉트 될 URL
            .encode() // 3. 파라미터 값을 URL 인코딩
            .build()
            .toUri() // 4. 최종적으로 URI 객체로 변환
    }

    suspend fun getKakaoToken(redirectDto: RedirectDto): KakaoTokenResDto {
        val formData = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code") // 고정 값
            add("client_id", apiKey)
            add("redirect_uri", KakaoUrl.LOGIN_URL)
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

    suspend fun getUserInfo(accessToken: String): KaKaoUserInfoRes {
        return webClient.get()
            .uri(KakaoUrl.KAKAO_USER_INFO_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .awaitBody<KaKaoUserInfoRes>()
    }

    // 카카오 연결 해제 (개발 테스트 용도 API)
    suspend fun unlinkByAdmin(userId: Long): UnlinkResDto {

        val formData = LinkedMultiValueMap<String, String>().apply {
            add("target_id_type", "user_id")
            add("target_id", userId.toString())
        }

        return webClient.post()
            .uri(KakaoUrl.UNLINK_URL)
            .header(HttpHeaders.AUTHORIZATION, "KakaoAK $adminKey")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .retrieve()
            .awaitBody<UnlinkResDto>()
    }

    // 로그인 처리 (가입 여부 확인)
    fun login(userId: Long) : Boolean {
        return authRepository.existsById(userId)
    }

    // 회원가입 처리
    fun register(registerResDto: RegisterResDto, userInfoRes: KaKaoUserInfoRes) {
        val newUser = User(
            id = userInfoRes.id,
            name = registerResDto.name
        )
        .apply {
            this.profileImageUrl = userInfoRes.kakaoProperties?.profileImage
            // 로그인 타입은 "KAKAO"로 고정하거나, 다른 로직이 있다면 추가
            this.loginType = "KAKAO"
        }

        authRepository.save(newUser)
    }
}