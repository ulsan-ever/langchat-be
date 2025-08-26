package com.example.langchat.user.dto

import com.example.langchat.user.entity.User
import jakarta.validation.constraints.*

data class RegisterReqDto (
    @field:NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "유효한 이메일 형식이 아닙니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    val password: String,

    @field:NotBlank(message = "이름은 필수 입력 항목입니다.")
    val name: String
) {
    fun toEntity() : User {
        return User(
            userId = 0, // DB에서 자동 생성되므로 초기값은 0으로 설정
            email = this.email,
            password = this.password,
            name = this.name
        )
    }
}