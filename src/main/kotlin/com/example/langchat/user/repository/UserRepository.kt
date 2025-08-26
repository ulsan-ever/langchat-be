package com.example.langchat.user.repository

import com.example.langchat.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

// User 엔티티에 대한 CRUD 작업을 처리하는 리포지토리 인터페이스
interface UserRepository : JpaRepository<User, Int> {

}