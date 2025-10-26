package com.example.langchat.config.filter

import com.example.langchat.util.jwt.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter (
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header: String = request.getHeader("Authorization") // 인증 헤더를 가져온다.

        if (header.isBlank() || !header.startsWith("Bearer ")) {
            throw RuntimeException("Header 에 Bearer 토큰이 존재하지 않습니다.")
        }

        val token = header.substringAfter("Bearer ").trim()
        try {
            // token 검증 및 token 에 들어간 subject (userId) 추출
            val userId = tokenProvider.validateTokenAndGetSubject(token)
            if(!userId.isNullOrBlank()) {
                val auth = UsernamePasswordAuthenticationToken(
                    userId,
                    null, // jwt 에서는 추가 정보가 필요 없으므로 null 설정
                    listOf(SimpleGrantedAuthority("ROLE_USER")) // 권한 설정
                )
                SecurityContextHolder.getContext().authentication = auth
            }
        } catch (e : Exception) {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}