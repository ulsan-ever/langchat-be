package com.example.langchat.auth.controller

import com.example.langchat.auth.dto.*
import com.example.langchat.auth.dto.kakao.KaKaoUserInfoRes
import com.example.langchat.auth.dto.kakao.KakaoAccessTokenDto
import com.example.langchat.auth.dto.kakao.KakaoTokenResDto
import com.example.langchat.auth.service.AuthService
import com.example.langchat.util.jwt.TokenProvider
import jakarta.validation.Valid
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

@RestController
@RequestMapping("/api/auth/kakao")
class AuthController (
    private val authService: AuthService,
    private val tokenProvider: TokenProvider,
) {
    // 회원가입이 되어있지 않은 경우, kakao 정보를 임시적으로 저장해놓는 공간 (운영 환경에서는 redis 등을 사용)
    private val tempTokenStorage : ConcurrentHashMap<Long, KaKaoUserInfoRes> = ConcurrentHashMap()

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
    // * kakao accessToken 은 클라이언트 측에서 처리해서 넘겨준다. (/login 참조)
    @GetMapping("/login_server_test")
    suspend fun handleRedirect(@ModelAttribute redirectDto: RedirectDto): AuthResult {

        // 토큰 요청에 필요한 인가 코드를 받았으면 로그인을 진행한다.
        redirectDto.code?.let {
            val kakaoTokenResDto: KakaoTokenResDto = authService.getKakaoToken(redirectDto) // 인가 코드를 사용하여 토큰 요청한다.
            val userInfoResDto = authService.getUserInfo(kakaoTokenResDto.accessToken)      // 토큰 정보로 사용자 정보 조회한다.
            val result = authService.login(userInfoResDto.id) // 회원정보를 조회해서 서비스에 가입되어있는지 확인한다.

            if(!result) { // 회원가입이 안된 경우
                tempTokenStorage[userInfoResDto.id] = userInfoResDto // 임시로 데이터를 저장해둔다.
                return AuthResult.SignupRequired(tokenProvider.createAccessToken(userInfoResDto.id.toString())) // 임시 token 정보 제공
            }
            // 회원가입이 된 경우
            val accessToken = tokenProvider.createAccessToken(userInfoResDto.id.toString())
            val refreshToken = tokenProvider.createRefreshToken() // db 에 저장 필요
            return AuthResult.LoginSuccess(accessToken, refreshToken, userInfoResDto)
        }

        throw Exception("No authorization code received")
    }

    @GetMapping("/login")
    suspend fun login (@Valid @RequestBody kakaoAccessTokenDto: KakaoAccessTokenDto): AuthResult {
        val userInfoResDto = authService.getUserInfo(kakaoAccessTokenDto.accessToken) // 토큰 정보로 사용자 정보 조회한다.
        val isRegistered = authService.login(userInfoResDto.id) // 회원정보를 조회해서 서비스에 가입되어있는지 확인한다.

        if(!isRegistered) { // 회원가입이 안된 경우
            tempTokenStorage[userInfoResDto.id] = userInfoResDto // 임시로 데이터를 저장해둔다.
            return AuthResult.SignupRequired(tokenProvider.createAccessToken(userInfoResDto.id.toString())) // 임시 token 정보 제공
        }
        // 회원가입이 된 경우
        val accessToken = tokenProvider.createAccessToken(userInfoResDto.id.toString())
        val refreshToken = tokenProvider.createRefreshToken() // redis 저장 필요
        return AuthResult.LoginSuccess(accessToken, refreshToken, userInfoResDto)
    }

    @PostMapping("/register") // 회원가입이 안된 경우 이 API 로 요청한다.
    fun register(@RequestBody registerResDto: RegisterResDto) : AuthResult {

        // token 이 유효하다면
        tokenProvider.validateTokenAndGetSubject(registerResDto.signUpToken)?.let { it ->
            val userInfo = tempTokenStorage[it.toLong()]
            userInfo.let {
                authService.register(registerResDto, it!!)
            }

            // 회원가입이 완료되었으면 로그인 처리
            val accessToken = tokenProvider.createAccessToken(it)
            val refreshToken = tokenProvider.createRefreshToken() // db 에 저장 필요
            tempTokenStorage.remove(it.toLong())

            return  AuthResult.LoginSuccess(accessToken, refreshToken, userInfo)
        }

        throw Exception("No authorization code received")
    }

    @PostMapping("/unlink")
    suspend fun unlinkByAdmin (@RequestParam userId:Long) : UnlinkResDto {
        return authService.unlinkByAdmin(userId)
    }
}