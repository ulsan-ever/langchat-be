package com.example.langchat.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

class KaKaoUserInfoRes (
    // 사용자 회원번호
    val id: Long,

    // 서비스에 연결 완료된 시각
    @JsonProperty("connected_at")
    val connectedAt: String,

    // 카카오계정 정보
//    @JsonProperty("kakao_account")
//    val kakaoAccount: KakaoAccount?,

    // 사용자 프로퍼티
    val properties: Map<String, String>?,

    // 카카오싱크 간편가입을 통해 로그인한 시각 -> 가입시간
    @JsonProperty("synched_at")
    val synchedAt: String?,

    // 파트너를 위한 추가 정보
//    @JsonProperty("for_partner")
//    val forPartner: Partner?
)