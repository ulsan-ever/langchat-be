package com.example.langchat.util.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenProvider (
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.secret-key}") private val secretKeyString: String,
    @Value("\${jwt.access-token-expiration-minutes}") private val accessTokenExpirationMinutes: Long,
    @Value("\${jwt.refresh-token-expiration-days}") private val refreshTokenExpirationDays: Long,
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secretKeyString.toByteArray())

    // Access Token 생성
    fun createAccessToken(userSpecification: String): String {
        return createToken(userSpecification, accessTokenExpirationMinutes, ChronoUnit.MINUTES)
    }

    // Refresh Token 생성 (Subject 없이 만료 시간만 길게 설정)
    fun createRefreshToken(): String {
        return createToken(null, refreshTokenExpirationDays, ChronoUnit.DAYS)
    }

    private fun createToken(subject: String?, expiration: Long, unit: ChronoUnit): String {
        val now = Instant.now()
        val expirationDate = now.plus(expiration, unit)

        val builder = Jwts.builder()
            .signWith(secretKey, Jwts.SIG.HS512)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expirationDate))

        subject?.let { builder.subject(it) } // 유저 정보가 있으면 설정

        return builder.compact() // 토큰을 만들어 반환
    }

    fun validateTokenAndGetSubject(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (e: Exception) {
            // 유효하지 않은 토큰
            null
        }
    }

}