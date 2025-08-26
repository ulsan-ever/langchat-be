package com.example.langchat.user.entity

import com.example.langchat.util.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
@SequenceGenerator( // JPA에서 시퀀스 전략을 사용하기 위한 설정
    name = "USERS_SEQ_GENERATOR", // 시퀀스 제너레이터 이름
    sequenceName = "users_user_id_seq", // 데이터베이스에 생성될 시퀀스 이름
    allocationSize = 1 // 시퀀스 증가 값
)
class User (
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ_GENERATOR") // 시퀀스 전략을 사용하여 id 자동 생성
    @Column(name = "user_id")
    val userId: Int,

    @Column
    var email: String,

    @Column
    var password: String,

    @Column
    var name: String,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null
) : BaseTimeEntity()