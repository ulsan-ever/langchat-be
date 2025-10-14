package com.example.langchat.auth.repository

import com.example.langchat.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<User, Long> {
}