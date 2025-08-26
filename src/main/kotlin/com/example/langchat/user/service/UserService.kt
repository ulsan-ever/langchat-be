package com.example.langchat.user.service

import com.example.langchat.user.dto.RegisterReqDto
import com.example.langchat.user.dto.RegisterResDto
import com.example.langchat.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository
) {
    @Transactional
    fun register(registerReqDto: RegisterReqDto) : RegisterResDto {
        // 회원가입 로직 구현 (예: 비밀번호 해싱, 중복 체크 등)
        val savedUser = userRepository.save(registerReqDto.toEntity())
        return RegisterResDto.from(savedUser) // 저장된 User 엔티티를 DTO로 변환하여 반환
    }
}