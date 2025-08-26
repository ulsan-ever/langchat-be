package com.example.langchat.user.controller

import com.example.langchat.user.dto.RegisterReqDto
import com.example.langchat.user.dto.RegisterResDto
import com.example.langchat.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(("/api/user"))
class UserController (
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody registerReqDto: RegisterReqDto) : ResponseEntity<RegisterResDto> {
        // 회원가입 처리
        val savedUser = userService.register(registerReqDto)
        return ResponseEntity.ok(savedUser)
    }
}