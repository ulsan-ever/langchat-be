package com.example.langchat.auth.entity

import com.example.langchat.util.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    @Column(name = "user_id")
    val id: Long,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null,

    @Column(name = "login_type")
    var loginType: String? = null

) : BaseTimeEntity()