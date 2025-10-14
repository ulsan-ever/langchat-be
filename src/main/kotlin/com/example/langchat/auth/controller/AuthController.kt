package com.example.langchat.auth.controller

import com.example.langchat.auth.dto.*
import com.example.langchat.auth.dto.kakao.KaKaoUserInfoRes
import com.example.langchat.auth.dto.kakao.KakaoTokenResDto
import com.example.langchat.auth.service.AuthService
import com.example.langchat.util.jwt.TokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@RestController
@RequestMapping("/api/kakao")
class AuthController (
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
) {
    private val tempStorage : ConcurrentHashMap<Long, KaKaoUserInfoRes> = ConcurrentHashMap()

    // 1. 카카오 인가 코드 요청
    @GetMapping("/authorize")
    fun authorize(): ResponseEntity<Void> {
        val destinationUri = authService.makeAuthorizeUrl()

        // 302 (FOUND) Redirect 응답 생성
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(destinationUri)
            .build()
    }

    // 2. 카카오가 사용자를 이리로 돌려보냄 (인가 코드와 함께)
    @GetMapping("/login")
    suspend fun handleRedirect(@ModelAttribute redirectDto: RedirectDto): AuthResult {

        // 토큰 요청에 필요한 인가 코드를 받았으면 로그인을 진행한다.
        redirectDto.code?.let {
            val kakaoTokenResDto: KakaoTokenResDto = authService.getKakaoToken(redirectDto) // 인가 코드를 사용하여 토큰 요청한다.
            val userInfoResDto = authService.getUserInfo(kakaoTokenResDto.accessToken)      // 토큰 정보로 사용자 정보 조회한다.
            val result = authService.login(userInfoResDto.id) // 회원정보를 조회해서 서비스에 가입되어있는지 확인한다.

            if(!result) { // 회원가입이 안된 경우
                tempStorage[userInfoResDto.id] = userInfoResDto // 임시로 데이터를 저장해둔다.
                return AuthResult.SignupRequired(tokenProvider.createAccessToken(userInfoResDto.id.toString())) // 임시 token 정보 제공
            }
            // 회원가입이 된 경우
            val accessToken = tokenProvider.createAccessToken(userInfoResDto.id.toString())
            val refreshToken = tokenProvider.createRefreshToken() // db 에 저장 필요
            return AuthResult.LoginSuccess(accessToken, refreshToken, userInfoResDto)
        }

        throw Exception("No authorization code received")
    }

    @PostMapping("/register") // 회원가입이 안된 경우 이 API 로 요청한다.
    fun register(@RequestBody registerResDto: RegisterResDto) : AuthResult {

        // token 이 유효하다면
        tokenProvider.validateTokenAndGetSubject(registerResDto.signUpToken)?.let { it ->
            val userInfo = tempStorage[it.toLong()]
            userInfo.let {
                authService.register(registerResDto, it!!)
            }

            // 회원가입이 완료되었으면 로그인 처리
            val accessToken = tokenProvider.createAccessToken(it)
            val refreshToken = tokenProvider.createRefreshToken() // db 에 저장 필요
            tempStorage.remove(it.toLong())

            return  AuthResult.LoginSuccess(accessToken, refreshToken, userInfo)
        }

        throw Exception("No authorization code received")
    }

    @PostMapping("/unlink")
    suspend fun unlinkByAdmin (@RequestParam userId:Long) : UnlinkResDto {
        return authService.unlinkByAdmin(userId)
    }
}