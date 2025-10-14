package com.example.langchat.auth.dto.kakao

import com.fasterxml.jackson.annotation.JsonProperty

class KaKaoUserInfoRes (
    // 사용자 회원번호
    val id: Long,

    // 서비스에 연결 완료된 시각
    @JsonProperty("connected_at")
    val connectedAt: String,

    // 카카오계정 정보
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount?,

    // 사용자 프로퍼티
    @JsonProperty("properties")
    val kakaoProperties: KakaoProperties?,
)