//package com.example.langchat.config.filter
//
//import com.example.langchat.util.jwt.TokenProvider
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//
//@Component
//class JwtAuthenticationFilter (private val tokenProvider: TokenProvider) : OncePerRequestFilter() {
//
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//        val token = resolveToken(request)
//
//        if (token != null) {
//            val subject = tokenProvider.validateTokenAndGetSubject(token)
//            if (subject != null) {
//                // "username:ROLE" 형식으로 저장된 subject 파싱
//                val (username, role) = subject.split(":")
//                val authorities = listOf(SimpleGrantedAuthority(role))
//                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
//                SecurityContextHolder.getContext().authentication = authentication
//            }
//        }
//        filterChain.doFilter(request, response)
//    }
//
//    // 헤더에서 토큰을 추출한다.
//    private fun resolveToken(request: HttpServletRequest): String? {
//        val bearerToken = request.getHeader("Authorization")
//        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            bearerToken.substring(7)
//        } else {
//            null
//        }
//    }
//}